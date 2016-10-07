package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.kezarszy.tankwar.Game;
import com.kezarszy.tankwar.levels.Level;
import com.kezarszy.tankwar.sounds.Sounds;

import java.util.ArrayList;

public class Tank {
    public static final float SIZE = 0.75f;
    public static final float SPEED = 2f;
    public static final int ROTATION_SPEED = 2;

    public static final int CANON_WIDTH = 58;
    public static final int CANON_HEIGHT = 24;

    protected boolean isAlive, isHurt;

    protected float x, y;
    protected double dirx, diry;
    protected int angle, health;

    protected Texture tank;
    protected TextureRegion tankRegion;

    private Level level;

    protected Sounds sounds;

    protected ArrayList<Bullet> bullets;

    protected long firingTimer;
    protected long firingDelay;

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

        sounds = new Sounds();
        sounds.loadTankSounds();

        bullets = new ArrayList<Bullet>();
        firingTimer = System.nanoTime();
        firingDelay = 500;

        shapeRenderer = new ShapeRenderer();
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void setLevel(Level level) {this.level = level;}

    public int getHealth() {return this.health;}
    public void setHealth(int health) {this.health = health;}

    public boolean getIsAlive() {return this.isAlive;}

    public boolean getIsHurt() {return this.isHurt;}
    public void setIsHurt(boolean isHurt) {this.isHurt = isHurt;}

    public Polygon getCollisionPoly() {return this.collisionPoly;}

    public ArrayList<Bullet> getBullets() {return this.bullets;}

    public void update(ArrayList<Tank> tanks){
        // PLAYER MOVEMENT LOGIC
        this.dirx = Math.cos(Math.toRadians(angle));
        this.diry = Math.sin(Math.toRadians(angle));

        for(int i=0; i<bullets.size(); i++)
            bullets.get(i).update();
        bulletCollisionDetection();

        collisionPoly.setRotation(angle);
        canonPoly.setRotation(angle);
        collisionPrediction.setRotation(angle);

        tanksCollisionDetection(tanks);
        if(this.health == 0){
            sounds.playExplosionSound();
            isAlive = false;
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

    public boolean collisionDetection(Polygon poly, ArrayList<Tank> tanks){
        ArrayList<Polygon> shapes = level.getColisionPoly();
        for(int i=0; i<shapes.size(); i++)
            if(Intersector.overlapConvexPolygons(poly, shapes.get(i)))
                return true;
        for(Tank tank: tanks)
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

    public void tanksCollisionDetection(ArrayList<Tank> tanks){
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
}
