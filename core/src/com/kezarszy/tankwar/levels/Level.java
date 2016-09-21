package com.kezarszy.tankwar.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.viewport.*;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;

public class Level {

    private SceneLoader sceneLoader;
    private ItemWrapper root;

    private ArrayList<Polygon> colisionPoly;
    private ShapeRenderer shapeRenderer;

    public Level(Viewport viewport){
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene("MainScene",viewport);
        root = new ItemWrapper(sceneLoader.getRoot());

        colisionPoly = new ArrayList<Polygon>();
        colisionShapes();
        shapeRenderer = new ShapeRenderer();
    }

    public ArrayList<Polygon> getColisionPoly() {return this.colisionPoly;}

    public void render(SpriteBatch sb){
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 0);
        for(int i=0; i<10; i++)
            shapeRenderer.polygon(colisionPoly.get(i).getTransformedVertices());
        shapeRenderer.end();
    }

    public void colisionShapes(){
        for(int i=1; i<11; i++){
            int width = 66, height = 44;
            TransformComponent aux = root.getChild("sandBag"+i).getEntity().getComponent(TransformComponent.class);
            float[] polyVertices = new float[]{aux.x, aux.y,
                aux.x, aux.y + height,
                aux.x + width, aux.y + height,
                aux.x + width, aux.y};
            Polygon poly = new Polygon(polyVertices);
            poly.setOrigin(aux.x + width/2, aux.y + height/2);
            poly.setRotation(aux.rotation);
            colisionPoly.add(poly);
        }
    }
}
