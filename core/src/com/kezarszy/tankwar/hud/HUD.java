package com.kezarszy.tankwar.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kezarszy.tankwar.entities.Player;
import com.kezarszy.tankwar.entities.Tank;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class HUD {

    private SceneLoader sceneLoader;
    private ItemWrapper root;

    private Player player;

    public HUD(){
        Viewport viewport = new StretchViewport(960,540);
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene("UI",viewport);
        root = new ItemWrapper(sceneLoader.getRoot());
    }

    public void setPlayer(Player player) {this.player = player;}

    public void render() {
        root.getChild("score").getEntity().getComponent(LabelComponent.class).setText("" + 100);
        if(!player.getIsAlive())root.getChild("healthBar").getEntity().getComponent(TransformComponent.class).x = 10000f;
        else root.getChild("healthBar").getEntity().getComponent(DimensionsComponent.class).width = player.getHealth();
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
    }
}
