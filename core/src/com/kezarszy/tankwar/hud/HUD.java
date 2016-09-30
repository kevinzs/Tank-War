package com.kezarszy.tankwar.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class HUD {

    private SceneLoader sceneLoader;
    private ItemWrapper root;

    public HUD(){
        Viewport viewport = new StretchViewport(960,540);
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene("UI",viewport);
        root = new ItemWrapper(sceneLoader.getRoot());
    }

    public void render(){
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());

        root.getChild("score").getEntity().getComponent(LabelComponent.class).setText(""+100);
    }
}
