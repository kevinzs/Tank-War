package com.kezarszy.tankwar.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class MenuState extends State {

    private SceneLoader sceneLoader;
    private ItemWrapper root;

    public MenuState(GameStateManager gsm, AssetManager assetManager){
        super(gsm, assetManager);

        Viewport viewport = new StretchViewport(960,540);
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene("menu",viewport);
        root = new ItemWrapper(sceneLoader.getRoot());

        loadButtons();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(SpriteBatch sb) {
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {

    }

    private void loadButtons() {
        sceneLoader.addComponentsByTagName("singlePlayer", ButtonComponent.class);
        root.getChild("singlePlayer").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {}

            @Override
            public void touchDown() {}

            @Override
            public void clicked() {}
        });
        sceneLoader.addComponentsByTagName("multiPlayer", ButtonComponent.class);
        root.getChild("multiPlayer").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {}

            @Override
            public void touchDown() {}

            @Override
            public void clicked() { gsm.push(new PlayState(gsm,manager)); }
        });
        sceneLoader.addComponentsByTagName("options", ButtonComponent.class);
        root.getChild("options").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {}

            @Override
            public void touchDown() {}

            @Override
            public void clicked() {}
        });
        sceneLoader.addComponentsByTagName("exit", ButtonComponent.class);
        root.getChild("exit").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {}

            @Override
            public void touchDown() {}

            @Override
            public void clicked() {Gdx.app.exit();}
        });
    }
}
