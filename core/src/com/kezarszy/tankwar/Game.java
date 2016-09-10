package com.kezarszy.tankwar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.entities.Tank;

public class Game {

    public Tank tank;

    public Game(){
        tank = new Tank(500,250);
    }

    public void update(){
        tank.update();
    }

    public void draw(SpriteBatch sb){
        tank.draw(sb);
    }
}
