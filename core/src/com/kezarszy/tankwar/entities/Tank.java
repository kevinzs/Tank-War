package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tank {

    public float x, y;
    public double dirx, diry;
    public int angle;
    public static final float SIZE = 0.75f;
    public static final float SPEED = 1.5f;
    public Texture tex;
    public TextureRegion texture;

    public Tank(int x, int y) {
        this.x = x; this.y = y;
        angle = 0;
        tex = new Texture("tankBlue.png");
        texture = new TextureRegion(tex);
    }

    public void update(){
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            this.x += this.dirx * SPEED;
            this.y += this.diry * SPEED;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            this.x -= this.dirx * SPEED;
            this.y -= this.diry * SPEED;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle = (angle + 1) % 360;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(angle >= 0) angle -= 1;
            else angle = 360;
        }
        this.dirx = Math.cos(Math.toRadians(angle));
        this.diry = Math.sin(Math.toRadians(angle));
    }

    public void draw(SpriteBatch sb){
        sb.draw(texture, this.x, this.y, (tex.getWidth()*SIZE)/2, (tex.getHeight()*SIZE)/2,
                tex.getWidth()*SIZE, tex.getHeight()*SIZE,1,1,angle);
    }
}
