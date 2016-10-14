package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.kezarszy.tankwar.states.State;

public class Enemy extends Tank{

    public Enemy(int x, int y, State currentState){
        super(x,y);
        super.setState(currentState);

        tank = currentState.getManager().get("tankRed.png", Texture.class);
        tankRegion = new TextureRegion(tank);

        polyVertices = new float[]{this.x, this.y,
                this.x, this.y + tank.getHeight() * SIZE,
                this.x + tank.getWidth() * SIZE - 10, this.y + tank.getHeight() * SIZE,
                this.x + tank.getWidth() * SIZE - 10, this.y};
        collisionPoly = new Polygon(polyVertices);
        collisionPoly.setOrigin(this.x + (tank.getWidth()*SIZE-10)/2, this.y + (tank.getHeight()*SIZE)/2);


        int auxX = 26, auxY = 23;
        canonVertices = new float[]{this.x + auxX, this.y + auxY,
                this.x + auxX, this.y + auxY + CANON_HEIGHT * SIZE - 3,
                this.x + auxX + CANON_WIDTH * SIZE - 3, this.y + auxY + CANON_HEIGHT * SIZE - 3,
                this.x + auxX + CANON_WIDTH * SIZE - 3, this.y + auxY};
        canonPoly = new Polygon(canonVertices);
        canonPoly.setOrigin(this.x + auxX, this.y + auxY + (CANON_HEIGHT*SIZE-3)/2);

        collisionPrediction = new Polygon(polyVertices);
        collisionPrediction.setOrigin(this.x + (tank.getWidth()*SIZE-10)/2, this.y + (tank.getHeight()*SIZE)/2);
    }

    public void firing(){

    }

    public void dispose(){
        tank.dispose();
    }
}
