package com.kezarszy.tankwar.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.*;
import com.kezarszy.tankwar.Game;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.PolygonComponent;
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

        if(Game.DEBUG){
            shapeRenderer.setProjectionMatrix(sb.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 0);
            for(int i=0; i<colisionPoly.size(); i++)
                shapeRenderer.polygon(colisionPoly.get(i).getTransformedVertices());
            shapeRenderer.end();
        }
    }

    public void colisionShapes(){
        // SAND BAGS SHAPES
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

        // BARRELS SHAPES
        for(int i=1; i<7; i++){
            int width = 44, height = 62;
            TransformComponent aux = root.getChild("barrel"+i).getEntity().getComponent(TransformComponent.class);
            float[] polyVertices = new float[]{aux.x, aux.y,
                    aux.x, aux.y + height,
                    aux.x + width, aux.y + height,
                    aux.x + width, aux.y};
            Polygon poly = new Polygon(polyVertices);
            poly.setOrigin(aux.x + width/2, aux.y + height/2);
            poly.setRotation(aux.rotation);
            colisionPoly.add(poly);
        }

        // BIG TREES SHAPES
        for(int j=1; j<4; j++){
            TransformComponent aux = root.getChild("bigTree"+j).getEntity().getComponent(TransformComponent.class);
            Vector2[][] aux2 = root.getChild("bigTree1").getEntity().getComponent(PolygonComponent.class).vertices;
            float[] polyVertices = new float[16];
            int cont = 0;
            for(int i=0; i<aux2[0].length; i++){
                polyVertices[cont] = aux.x + aux2[0][i].x; cont++;
                polyVertices[cont] = aux.y + aux2[0][i].y; cont++;
            }
            Polygon poly = new Polygon(polyVertices);
            poly.setOrigin(aux.x + 98/2, aux.y + 107/2);
            poly.setRotation(aux.rotation);
            colisionPoly.add(poly);
        }

        // SMALL TREES SHAPES
        for(int i=1; i<3; i++){
            TransformComponent aux = root.getChild("smallTree"+i).getEntity().getComponent(TransformComponent.class);
            Vector2[][] aux2 = root.getChild("smallTree1").getEntity().getComponent(PolygonComponent.class).vertices;
            float[] polyVertices = new float[16];
            int cont = 0;
            for(int j=0; j<aux2[0].length; j++){
                polyVertices[cont] = aux.x + aux2[0][j].x; cont++;
                polyVertices[cont] = aux.y + aux2[0][j].y; cont++;
            }
            Polygon poly = new Polygon(polyVertices);
            poly.setOrigin(aux.x + 87/2, aux.y + 87/2);
            poly.setRotation(aux.rotation);
            colisionPoly.add(poly);
        }

        // CIRCULAR BARRELS SHAPES
        for(int i=1; i<5; i++){
            TransformComponent aux = root.getChild("circularBarrel"+i).getEntity().getComponent(TransformComponent.class);
            Vector2[][] aux2 = root.getChild("circularBarrel1").getEntity().getComponent(PolygonComponent.class).vertices;
            float[] polyVertices2 = new float[16];
            int cont = 0;
            for(int j=0; j<aux2[0].length; j++){
                polyVertices2[cont] = aux.x + aux2[0][j].x; cont++;
                polyVertices2[cont] = aux.y + aux2[0][j].y; cont++;
            }
            Polygon poly = new Polygon(polyVertices2);
            poly.setOrigin(aux.x + 48/2, aux.y + 48/2);
            poly.setRotation(aux.rotation);
            colisionPoly.add(poly);
        }
    }
}
