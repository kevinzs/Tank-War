package com.kezarszy.tankwar.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.entities.Tank;
import com.kezarszy.tankwar.levels.Level;

public class PlayState extends State {

    public Tank tank;
    public Level level;

    public PlayState(GameStateManager gsm){
        super(gsm);
        tank = new Tank(100,100);
        level = new Level(this.viewport);
        tank.setLevel(level);
    }

    public void update(){
        super.update();
        tank.update();
        updateCam();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        level.render(sb);
        tank.draw(sb);
    }

    public void updateCam(){
        if((tank.getX() > 480 && tank.getX() < 520) && (tank.getY() > 270 && tank.getY() < 725))
            this.cam.position.set(tank.getX(), tank.getY(), 0);
        if(tank.getX() > 480 && tank.getX() < 520)
            this.cam.position.set(tank.getX(), cam.position.y, 0);
        if(tank.getY() > 270 && tank.getY() < 725)
            this.cam.position.set(cam.position.x, tank.getY(), 0);

    }
}
