package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.kezarszy.tankwar.Game;
import com.kezarszy.tankwar.levels.Level;
import com.kezarszy.tankwar.states.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Tank {
    public static final float SIZE = 0.75f;
    public static final float SPEED = 1f;
    public static final int ROTATION_SPEED = 1;

    public static final int CANON_WIDTH = 58;
    public static final int CANON_HEIGHT = 24;

    protected boolean isAlive, isHurt;

    protected float x, y;
    protected double dirx, diry;
    protected int angle, health;

    protected Texture tank;
    protected TextureRegion tankRegion;

    protected Level level;

    protected State currentState;

    protected ArrayList<Bullet> bullets;

    protected long firingTimer;
    protected long firingDelay;
    protected boolean firing;

    protected Polygon collisionPoly;
    protected float[] polyVertices;
    protected Polygon canonPoly;
    protected float[] canonVertices;
    protected float[] canonTransformedVertices;
    protected Polygon collisionPrediction;
    protected ShapeRenderer shapeRenderer;

    public Tank(int x, int y) {
        this.x = x; this.y = y;
        this.angle = 0; this.health = 100;
        this.isAlive = true;
        this.isHurt = false;

        bullets = new ArrayList<Bullet>();
        firingTimer = System.nanoTime();
        firingDelay = 500;

        if(Game.DEBUG) shapeRenderer = new ShapeRenderer();
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public int getRotation() {return this.angle;}
    public void setRotation(int rotation) {this.angle = rotation;}

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;

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

    public void setLevel(Level level) {this.level = level;}

    public int getHealth() {return this.health;}
    public void setHealth(int health) {this.health = health;}

    public boolean getIsAlive() {return this.isAlive;}

    public boolean getIsHurt() {return this.isHurt;}
    public void setIsHurt(boolean isHurt) {this.isHurt = isHurt;}

    public Polygon getCollisionPoly() {return this.collisionPoly;}

    public ArrayList<Bullet> getBullets() {return this.bullets;}

    public void setState(State state) {this.currentState = state;}

    public void update(HashMap<String,Tank> tanks){
        // PLAYER MOVEMENT LOGIC
        this.dirx = Math.cos(Math.toRadians(angle));
        this.diry = Math.sin(Math.toRadians(angle));

        for(int i=0; i<bullets.size(); i++) {
            bullets.get(i).update();
            if(bullets.get(i).getDelete()) bullets.remove(i);
        }
        bulletCollisionDetection();

        collisionPoly.setRotation(angle);
        canonPoly.setRotation(angle);
        collisionPrediction.setRotation(angle);

        tanksCollisionDetection(tanks.values());
        if(this.health == 0){
            Sound explosion = currentState.getManager().get("sounds/explode.wav", Sound.class);
            explosion.play(1.0f);
            isAlive = false;
            health = -10;
        }
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

    public boolean collisionDetection(Polygon poly, HashMap<String,Tank> tanks){
        ArrayList<Polygon> shapes = level.getColisionPoly();
        for(int i=0; i<shapes.size(); i++)
            if(Intersector.overlapConvexPolygons(poly, shapes.get(i)))
                return true;
        for(Tank tank: tanks.values())
            if(!tank.equals(this))
                if(Intersector.overlapConvexPolygons(poly, tank.getCollisionPoly()))
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

    public void tanksCollisionDetection(Collection<Tank> tanks){
        for(Tank tank: tanks){
            if(!tank.equals(this)) {
                ArrayList<Bullet> tankBullets = tank.getBullets();
                for (Bullet bullet : tankBullets)
                    if (Intersector.overlapConvexPolygons(this.collisionPoly, bullet.getBulletPoly())) {
                        setHealth(this.health - 25);
                        this.setIsHurt(true);
                        tankBullets.remove(bullet);
                        break;
                    }
            }
        }
    }

    public void shooting(boolean shooting){
        if(shooting) {
            firing = true;
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if (elapsed > firingDelay) {
                playShootSound();
                firingTimer = System.nanoTime();
                canonTransformedVertices = canonPoly.getTransformedVertices();
                bullets.add(new Bullet((int) canonTransformedVertices[0], (int) canonTransformedVertices[1],
                        0, 0, angle, currentState.getManager().get("bulletRed.png", Texture.class)));
            }
        } else firing = false;
    }

    public void playShootSound(){
        Sound shoot = currentState.getManager().get("sounds/missile_explosion.ogg", Sound.class);
        shoot.play(1.0f);
    }

    public void dispose(){

    }
}
