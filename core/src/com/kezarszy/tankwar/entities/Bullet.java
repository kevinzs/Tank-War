package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bullet {

    public static final float SIZE = 0.75f;
    public static final float SPEED = 5f;

    private float x,y;
    private float originX, originY;
    private float angle;

    private Texture bullet;
    private TextureRegion bulletRegion;

    public Bullet(float x,float y, float originX, float originY, float angle){
        this.x = x; this.y = y;
        this.originX = originX; this.originY = originY;
        this.angle = angle;

        bullet = new Texture("bulletBlue.png");
        bulletRegion = new TextureRegion(bullet);
    }

    public void update(){
        this.x += Math.cos(Math.toRadians(angle)) * SPEED;
        this.y += Math.sin(Math.toRadians(angle)) * SPEED;
    }

    public void draw(SpriteBatch sb){
        sb.draw(bulletRegion, this.x, this.y, originX, originY,
                bullet.getWidth()*SIZE, bullet.getHeight()*SIZE,1,1,angle);
    }
}
