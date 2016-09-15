package com.kezarszy.tankwar.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.*;
import com.kezarszy.tankwar.TankWar;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class Level {

    private SceneLoader sceneLoader;
    private ItemWrapper root;

    public Level(){
        Viewport viewport = new StretchViewport(TankWar.WIDTH,TankWar.HEIGHT);
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene("MainScene",viewport);
        root = new ItemWrapper(sceneLoader.getRoot());
    }

    public void render(SpriteBatch sb){
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
    }
}
