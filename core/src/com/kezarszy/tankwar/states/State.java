package com.kezarszy.tankwar.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kezarszy.tankwar.TankWar;

public abstract class State {

    private GameStateManager gsm;

    protected OrthographicCamera cam;
    protected Viewport viewport;

    protected State(GameStateManager gsm){
        this.gsm = gsm;
        cam = new OrthographicCamera(TankWar.WIDTH, TankWar.HEIGHT);
        viewport = new StretchViewport(TankWar.WIDTH, TankWar.HEIGHT, cam);
        viewport.apply();

        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
    }

    protected GameStateManager getGsm() {return this.gsm;}

    public void update(){
        cam.update();
    }

    public abstract void render(SpriteBatch sb);
}
