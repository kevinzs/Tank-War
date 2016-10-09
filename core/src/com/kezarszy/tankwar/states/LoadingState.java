package com.kezarszy.tankwar.states;

import com.badlogic.gdx.Gdx;
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
        super(gsm);
        super.loadAssets();

        Viewport viewport = new StretchViewport(960,540);
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene("loading",viewport);
        root = new ItemWrapper(sceneLoader.getRoot());
    }

    @Override
    public void update() {
        if(manager.update())
            gsm.push(new PlayState(gsm));

        progress = manager.getProgress() * 100;
    }

    @Override
    public void render(SpriteBatch sb) {
        root.getChild("percent").getEntity().getComponent(LabelComponent.class).setText(progress + "%");
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
    }
}
