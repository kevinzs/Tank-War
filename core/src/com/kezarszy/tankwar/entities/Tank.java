package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.kezarszy.tankwar.Game;
import com.kezarszy.tankwar.levels.Level;

import java.util.ArrayList;

public class Tank {
    public static final float SIZE = 0.75f;
    public static final float SPEED = 2f;
    public static final int ROTATION_SPEED = 2;

    public static final int CANON_WIDTH = 58;
    public static final int CANON_HEIGHT = 24;

    private float x, y;
    private double dirx, diry;
    private int angle;

    private Texture tank;
    private TextureRegion tankRegion;

    private Level level;

    private ArrayList<Bullet> bullets;

    private long firingTimer;
    private long firingDelay;

    private Polygon collisionPoly;
    private float[] polyVertices;
    private Polygon canonPoly;
    private float[] canonVertices;
    private float[] canonTransformedVertices;
    private Polygon collisionPrediction;
    private ShapeRenderer shapeRenderer;

    public Tank(int x, int y) {
        this.x = x; this.y = y;
        angle = 0;

        tank = new Texture("tankBlue.png");
        tankRegion = new TextureRegion(tank);

        bullets = new ArrayList<Bullet>();
        firingTimer = System.nanoTime();
        firingDelay = 500;

        shapeRenderer = new ShapeRenderer();
        polyVertices = new float[]{this.x, this.y,
                                    this.x, this.y + tank.getHeight() * SIZE,
                                    this.x + tank.getWidth() * SIZE - 10, this.y + tank.getHeight() * SIZE,
                                    this.x + tank.getWidth() * SIZE - 10, this.y};
        collisionPoly = new Polygon(polyVertices);
        collisionPoly.setOrigin(this.x + (tank.getWidth()*SIZE-10)/2, this.y + (tank.getHeight()*SIZE)/2);


        int auxX = 26, auxY = 23;
        canonVertices = new float[]{this.x + auxX, this.y + auxY,
                this.x + auxX, this.y + auxY + CANON_HEIGHT * SIZE - 3,
                this.x + auxX + CANON_WIDTH * SIZE - 3, this.y + auxY + CANON_HEIGHT * SIZE - 3,
                this.x + auxX + CANON_WIDTH * SIZE - 3, this.y + auxY};
        canonPoly = new Polygon(canonVertices);
        canonPoly.setOrigin(this.x + auxX, this.y + auxY + (CANON_HEIGHT*SIZE-3)/2);

        collisionPrediction = new Polygon(polyVertices);
        collisionPrediction.setOrigin(this.x + (tank.getWidth()*SIZE-10)/2, this.y + (tank.getHeight()*SIZE)/2);
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void setLevel(Level level) {this.level = level;}

    public void update(){
        // PLAYER MOVEMENT LOGIC
        this.dirx = Math.cos(Math.toRadians(angle));
        this.diry = Math.sin(Math.toRadians(angle));

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            collisionPrediction.setPosition(collisionPoly.getX(), collisionPoly.getY());
            collisionPrediction.translate((float) (this.dirx * SPEED), (float) (this.diry * SPEED));
            if(!collisionDetection(collisionPrediction)){
                this.x += this.dirx * SPEED;
                this.y += this.diry * SPEED;
                collisionPoly.translate((float) this.dirx * SPEED, (float) this.diry * SPEED);
                canonPoly.translate((float) this.dirx * SPEED, (float) this.diry * SPEED);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            collisionPrediction.setPosition(collisionPoly.getX(), collisionPoly.getY());
            collisionPrediction.translate((float) (- this.dirx * SPEED), (float) (- this.diry * SPEED));
            if(!collisionDetection(collisionPrediction)){
                this.x -= this.dirx * SPEED;
                this.y -= this.diry * SPEED;
                collisionPoly.translate((float) -this.dirx * SPEED, (float) -this.diry * SPEED);
                canonPoly.translate((float) -this.dirx * SPEED, (float) -this.diry * SPEED);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle = (angle + ROTATION_SPEED) % 360;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(angle >= 0) angle -= ROTATION_SPEED;
            else angle = 360;
        }

        // FIRING LOGIC
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if(elapsed > firingDelay) {
                firingTimer = System.nanoTime();
                canonTransformedVertices = canonPoly.getTransformedVertices();
                bullets.add(new Bullet(canonTransformedVertices[0], canonTransformedVertices[1],
                        0, 0, angle));
            }
        }

        for(int i=0; i<bullets.size(); i++)
            bullets.get(i).update();
        bulletCollisionDetection();

        collisionPoly.setRotation(angle);
        canonPoly.setRotation(angle);
        collisionPrediction.setRotation(angle);
    }

    public void draw(SpriteBatch sb){
        for(int i=0; i<bullets.size(); i++)
            bullets.get(i).draw(sb);

        sb.begin();
        sb.draw(tankRegion, this.x, this.y, (tank.getWidth()*SIZE-10)/2, (tank.getHeight()*SIZE)/2,
                tank.getWidth()*SIZE, tank.getHeight()*SIZE,1,1,angle);
        sb.end();

        if(Game.DEBUG){
            shapeRenderer.setProjectionMatrix(sb.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 0);
            shapeRenderer.polygon(collisionPoly.getTransformedVertices());
            shapeRenderer.polygon(canonPoly.getTransformedVertices());
            shapeRenderer.end();
        }
    }

    public boolean collisionDetection(Polygon poly){
        ArrayList<Polygon> shapes = level.getColisionPoly();
        for(int i=0; i<shapes.size(); i++)
            if(Intersector.overlapConvexPolygons(poly, shapes.get(i)))
                return true;
        return false;
    }

    public void bulletCollisionDetection(){
        ArrayList<Polygon> shapes = level.getColisionPoly();
        for(int i=0; i<bullets.size(); i++)
            for(int j=0; j<shapes.size(); j++)
                if(Intersector.overlapConvexPolygons(bullets.get(i).getBulletPoly(), shapes.get(j))){
                    bullets.remove(i);
                    break;
                }
    }
}
