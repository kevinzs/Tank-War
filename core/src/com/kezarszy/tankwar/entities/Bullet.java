package com.kezarszy.tankwar.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.kezarszy.tankwar.Game;

public class Bullet {

    public static final float SIZE = 0.75f;
    public static final float SPEED = 5f;

    private float x,y;
    private float originX, originY;
    private float angle;

    private Texture bullet;
    private TextureRegion bulletRegion;

    private Polygon bulletPoly;
    private float[] polyVertices;
    private ShapeRenderer shapeRenderer;

    private boolean isPlayer = false;

    public Bullet(float x,float y, float originX, float originY, float angle, boolean isPlayer){
        this.x = x; this.y = y;
        this.originX = originX; this.originY = originY;
        this.angle = angle; this.isPlayer = isPlayer;

        if(this.isPlayer)
            bullet = new Texture("bulletBlue.png");
        else
            bullet = new Texture("bulletRed.png");
        bulletRegion = new TextureRegion(bullet);

        shapeRenderer = new ShapeRenderer();

        polyVertices = new float[]{this.x, this.y,
                this.x, this.y + bullet.getHeight() * SIZE,
                this.x + bullet.getWidth() * SIZE, this.y + bullet.getHeight() * SIZE,
                this.x + bullet.getWidth() * SIZE, this.y};
        bulletPoly = new Polygon(polyVertices);
        bulletPoly.setRotation(angle);
        bulletPoly.setOrigin(this.x, this.y);
    }

    public Polygon getBulletPoly() {return this.bulletPoly;}

    public void update(){
        this.x += Math.cos(Math.toRadians(angle)) * SPEED;
        this.y += Math.sin(Math.toRadians(angle)) * SPEED;
        bulletPoly.translate((float) Math.cos(Math.toRadians(angle)) * SPEED,
                (float) Math.sin(Math.toRadians(angle)) * SPEED);
    }

    public void draw(SpriteBatch sb){
        sb.begin();
        sb.draw(bulletRegion, this.x, this.y, originX, originY,
                bullet.getWidth()*SIZE, bullet.getHeight()*SIZE,1,1,angle);
        sb.end();

        if(Game.DEBUG){
            shapeRenderer.setProjectionMatrix(sb.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 0);
            shapeRenderer.polygon(bulletPoly.getTransformedVertices());
            shapeRenderer.end();
        }
    }
}
