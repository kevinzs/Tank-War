package com.kezarszy.tankwar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.states.GameStateManager;
import com.kezarszy.tankwar.states.LoadingState;
import com.kezarszy.tankwar.states.PlayState;

public class Game {

    public static final boolean DEBUG = false;
    private GameStateManager gsm;

    public Game(){
        gsm = new GameStateManager();
        gsm.push(new LoadingState(gsm));
    }

    public void update(){ gsm.update(); }

    public void draw(SpriteBatch sb){
        gsm.render(sb);
    }
}
