package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

import java.util.ArrayList;

public class Tank {
    public static final float SIZE = 0.75f;
    public static final float SPEED = 2f;
    public static final int ROTATION_SPEED = 2;

    public static final int CANON_WIDTH = 58;
    public static final int CANON_HEIGHT = 24;

    private boolean debug = false;

    private float x, y;
    private double dirx, diry;
    private int angle;

    private Texture tank;
    private TextureRegion tankRegion;

    private ArrayList<Bullet> bullets;

    private long firingTimer;
    private long firingDelay;

    private Polygon colisionPoly;
    private float[] polyVertices;
    private Polygon canonPoly;
    private float[] canonVertices;
    private float[] canonTransformedVertices;
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
        colisionPoly = new Polygon(polyVertices);
        colisionPoly.setOrigin(this.x + (tank.getWidth()*SIZE-10)/2, this.y + (tank.getHeight()*SIZE)/2);


        int auxX = 26, auxY = 23;
        canonVertices = polyVertices = new float[]{this.x + auxX, this.y + auxY,
                this.x + auxX, this.y + auxY + CANON_HEIGHT * SIZE - 3,
                this.x + auxX + CANON_WIDTH * SIZE - 3, this.y + auxY + CANON_HEIGHT * SIZE - 3,
                this.x + auxX + CANON_WIDTH * SIZE - 3, this.y + auxY};
        canonPoly = new Polygon(canonVertices);
        canonPoly.setOrigin(this.x + auxX, this.y + auxY + (CANON_HEIGHT*SIZE-3)/2);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void update(){
        // PLAYER MOVEMENT LOGIC
        this.dirx = Math.cos(Math.toRadians(angle));
        this.diry = Math.sin(Math.toRadians(angle));
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            this.x += this.dirx * SPEED;
            this.y += this.diry * SPEED;
            colisionPoly.translate((float) this.dirx * SPEED, (float) this.diry * SPEED);
            canonPoly.translate((float) this.dirx * SPEED, (float) this.diry * SPEED);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            this.x -= this.dirx * SPEED;
            this.y -= this.diry * SPEED;
            colisionPoly.translate((float) -this.dirx * SPEED, (float) -this.diry * SPEED);
            canonPoly.translate((float) -this.dirx * SPEED, (float) -this.diry * SPEED);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle = (angle + ROTATION_SPEED) % 360;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(angle >= 0) angle -= ROTATION_SPEED;
            else angle = 360;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.B))
            debug = !debug;

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

        colisionPoly.setRotation(angle);
        canonPoly.setRotation(angle);
    }

    public void draw(SpriteBatch sb){
        for(int i=0; i<bullets.size(); i++)
            bullets.get(i).draw(sb);

        sb.begin();
        sb.draw(tankRegion, this.x, this.y, (tank.getWidth()*SIZE-10)/2, (tank.getHeight()*SIZE)/2,
                tank.getWidth()*SIZE, tank.getHeight()*SIZE,1,1,angle);
        sb.end();

        if(debug){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 0);
            shapeRenderer.polygon(colisionPoly.getTransformedVertices());
            shapeRenderer.polygon(canonPoly.getTransformedVertices());
            shapeRenderer.end();
        }
    }
}
