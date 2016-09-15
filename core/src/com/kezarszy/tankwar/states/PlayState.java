package com.kezarszy.tankwar.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.entities.Tank;

public class PlayState extends State {

    public Tank tank;

    public PlayState(GameStateManager gsm){
        super(gsm);
        tank = new Tank(500,250);
    }

    public void update(){
        super.update();
        tank.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        tank.draw(sb);
    }
}
