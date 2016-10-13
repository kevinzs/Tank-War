package com.kezarszy.tankwar.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class LoadingState extends State{

    private SceneLoader sceneLoader;
    private ItemWrapper root;

    private float progress;

    public LoadingState(GameStateManager gsm){
        super(gsm, new AssetManager());

        Viewport viewport = new StretchViewport(960,540);
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene("loading",viewport);
        root = new ItemWrapper(sceneLoader.getRoot());

        manager.load("tankBlue.png", Texture.class);
        manager.load("tankRed.png", Texture.class);
        manager.load("sounds/battleThemeA.mp3", Sound.class);
        manager.load("sounds/missile_explosion.ogg", Sound.class);
        manager.load("sounds/explode.wav", Sound.class);
    }

    @Override
    public void update() {
        if(manager.update())
            gsm.push(new PlayState(gsm,manager));

        progress = manager.getProgress() * 100;
    }

    @Override
    public void render(SpriteBatch sb) {
        root.getChild("percent").getEntity().getComponent(LabelComponent.class).setText(progress + "%");
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {

    }
}
