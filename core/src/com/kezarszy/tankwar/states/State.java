package com.kezarszy.tankwar.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kezarszy.tankwar.TankWar;

public abstract class State {

    private GameStateManager gsm;

    protected OrthographicCamera cam;
    protected Viewport viewport;

    protected State(GameStateManager gsm){
        this.gsm = gsm;
        cam = new OrthographicCamera();
        viewport = new StretchViewport(TankWar.WIDTH, TankWar.HEIGHT,cam);
        viewport.apply();

        cam.position.set(TankWar.WIDTH, TankWar.HEIGHT,0);
    }

    protected GameStateManager getGsm() {return this.gsm;}

    public void update(){
        cam.update();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02;
        }
    }

    public abstract void render(SpriteBatch sb);
}
