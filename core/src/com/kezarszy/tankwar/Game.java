package com.kezarszy.tankwar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.entities.Tank;
import com.kezarszy.tankwar.levels.Level;

public class Game {

    public Tank tank;
    public Level level;

    public Game(){
        tank = new Tank(0,0);
        level = new Level();

    }

    public void update(){
        tank.update();
    }

    public void draw(SpriteBatch sb){

        tank.draw(sb);
        level.render(sb);
    }
}
