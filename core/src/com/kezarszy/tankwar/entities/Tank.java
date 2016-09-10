package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Tank {

    public static final float SIZE = 0.75f;
    public static final float SPEED = 2f;
    public static final int ROTATION_SPEED = 2;

    private float x, y;
    private double dirx, diry;
    private int angle;

    private Texture tank;
    private TextureRegion tankRegion;

    private ArrayList<Bullet> bullets;

    private long firingTimer;
    private long firingDelay;

    public Tank(int x, int y) {
        this.x = x; this.y = y;
        angle = 0;

        tank = new Texture("tankBlue.png");
        tankRegion = new TextureRegion(tank);

        bullets = new ArrayList<Bullet>();
        firingTimer = System.nanoTime();
        firingDelay = 400;
    }

    public void update(){
        // PLAYER MOVEMENT LOGIC
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            this.x += this.dirx * SPEED;
            this.y += this.diry * SPEED;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            this.x -= this.dirx * SPEED;
            this.y -= this.diry * SPEED;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle = (angle + ROTATION_SPEED) % 360;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(angle >= 0) angle -= ROTATION_SPEED;
            else angle = 360;
        }
        this.dirx = Math.cos(Math.toRadians(angle));
        this.diry = Math.sin(Math.toRadians(angle));

        // FIRING LOGIC
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if(elapsed > firingDelay) {
                firingTimer = System.nanoTime();
                bullets.add(new Bullet((this.x*2+tank.getWidth())/2, (this.y*2+tank.getHeight())/2, (tank.getWidth()*SIZE)/2, (tank.getHeight()*SIZE)/2, angle));
            }
        }

        for(int i=0; i<bullets.size(); i++)
            bullets.get(i).update();
    }

    public void draw(SpriteBatch sb){
        sb.draw(tankRegion, this.x, this.y, (tank.getWidth()*SIZE)/2, (tank.getHeight()*SIZE)/2,
                tank.getWidth()*SIZE, tank.getHeight()*SIZE,1,1,angle);
        for(int i=0; i<bullets.size(); i++)
            bullets.get(i).draw(sb);
    }
}
