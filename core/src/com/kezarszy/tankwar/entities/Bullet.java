package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bullet {

    public static final float SIZE = 0.75f;
    public static final float SPEED = 5f;

    private float x,y;
    private float centerX, centerY;
    private float angle;

    private Texture bullet;
    private TextureRegion bulletRegion;

    public Bullet(float x,float y, float centerX, float centerY, float angle){
        this.x = x; this.y = y;
        this.centerX = centerX; this.centerY = centerY;
        this.angle = angle;

        bullet = new Texture("bulletBlue.png");
        bulletRegion = new TextureRegion(bullet);
    }

    public void update(){
        this.x += Math.cos(Math.toRadians(angle)) * SPEED;
        this.y += Math.sin(Math.toRadians(angle)) * SPEED;
    }

    public void draw(SpriteBatch sb){
        sb.draw(bulletRegion, this.x, this.y, (bullet.getWidth()*SIZE)/2, (bullet.getHeight()*SIZE)/2,
                bullet.getWidth()*SIZE, bullet.getHeight()*SIZE,1,1,angle);
    }
}
