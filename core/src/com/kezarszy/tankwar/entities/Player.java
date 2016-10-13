package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.kezarszy.tankwar.states.State;

import java.util.HashMap;

public class Player extends Tank{

    private boolean hasMoved;

    public Player(int posx, int posy, State currentState){
        super(posx,posy);
        super.setState(currentState);

        tank = currentState.getManager().get("tankBlue.png", Texture.class);
        tankRegion = new TextureRegion(tank);

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

    public boolean hasMoved() {return this.hasMoved;}

    public void update(HashMap<String,Tank> tanks){
        super.update(tanks);

        hasMoved = false;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            collisionPrediction.setPosition(collisionPoly.getX(), collisionPoly.getY());
            collisionPrediction.translate((float) (this.dirx * SPEED), (float) (this.diry * SPEED));
            if (!collisionDetection(collisionPrediction, tanks)) {
                this.x += this.dirx * SPEED;
                this.y += this.diry * SPEED;
                collisionPoly.translate((float) this.dirx * SPEED, (float) this.diry * SPEED);
                canonPoly.translate((float) this.dirx * SPEED, (float) this.diry * SPEED);
                hasMoved = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            collisionPrediction.setPosition(collisionPoly.getX(), collisionPoly.getY());
            collisionPrediction.translate((float) (-this.dirx * SPEED), (float) (-this.diry * SPEED));
            if (!collisionDetection(collisionPrediction, tanks)) {
                this.x -= this.dirx * SPEED;
                this.y -= this.diry * SPEED;
                collisionPoly.translate((float) -this.dirx * SPEED, (float) -this.diry * SPEED);
                canonPoly.translate((float) -this.dirx * SPEED, (float) -this.diry * SPEED);
                hasMoved = true;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle = (angle + ROTATION_SPEED) % 360;
            hasMoved = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(angle >= 0) angle -= ROTATION_SPEED;
            else angle = 360;
            hasMoved = true;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
            setIsHurt(true);
            this.health -= 25;
        }

        // FIRING LOGIC
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if(elapsed > firingDelay) {
                playShootSound();
                firingTimer = System.nanoTime();
                canonTransformedVertices = canonPoly.getTransformedVertices();
                bullets.add(new Bullet(canonTransformedVertices[0], canonTransformedVertices[1],
                        0, 0, angle, true));
            }
        }
    }

    public void playShootSound(){
        Sound shoot = currentState.getManager().get("sounds/missile_explosion.ogg", Sound.class);
        shoot.play(1.0f);
    }

    public void dispose(){
        tank.dispose();
    }
}
