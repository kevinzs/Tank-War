package com.kezarszy.tankwar.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kezarszy.tankwar.TankWar;

public abstract class State {

    protected GameStateManager gsm;

    protected OrthographicCamera cam;
    protected Viewport viewport;

    protected AssetManager manager;

    protected State(GameStateManager gsm, AssetManager manager){
        this.gsm = gsm;
        this.manager = manager;
        cam = new OrthographicCamera();
        viewport = new StretchViewport(TankWar.WIDTH, TankWar.HEIGHT,cam);
        viewport.apply();

        cam.position.set(TankWar.WIDTH, TankWar.HEIGHT,0);
    }

    protected GameStateManager getGsm() {return this.gsm;}
    public AssetManager getManager() {return this.manager;}

    public abstract void update();
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
