package com.kezarszy.tankwar.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.entities.Tank;
import com.kezarszy.tankwar.hud.HUD;
import com.kezarszy.tankwar.levels.Level;
import com.kezarszy.tankwar.sounds.Sounds;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class PlayState extends State {

    private ArrayList<Tank> tanks;
    private Level level;

    private HUD hud;

    private Sounds sounds;

    private float duration;
    private float intensity;
    private float elapsed;
    private float shakeTimer;

    private float camX;
    private float camY;

    public PlayState(GameStateManager gsm){
        super(gsm);

        hud = new HUD();

        sounds = new Sounds();
        sounds.loadMusic();
        sounds.playGameMusic();

        level = new Level(this.viewport);

        tanks = new ArrayList<Tank>();
        tanks.add(new Tank(100,100,true));
        tanks.get(0).setPlayerMode();
        hud.setPlayer(tanks.get(0));
        tanks.add(new Tank(300,500,false));
        for(Tank tank: tanks)
            tank.setLevel(level);
    }

    public void update(){
        super.update();
        for(Tank tank: tanks) {
            tank.update(tanks);
            if(!tank.getIsAlive()){
                tanks.remove(tank);
                break;
            }
        }

        updateCam();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        level.render(sb);
        for(Tank tank: tanks)
            tank.draw(sb);
        hud.render();
    }

    public void updateCam(){
        for(Tank tank: tanks) {
            if(tank.isPlayer()) {
                if ((tank.getX() > 480 && tank.getX() < 520) && (tank.getY() > 270 && tank.getY() < 725))
                    this.cam.position.set(tank.getX(), tank.getY(), 0);
                if (tank.getX() > 480 && tank.getX() < 520)
                    this.cam.position.set(tank.getX(), cam.position.y, 0);
                if (tank.getY() > 270 && tank.getY() < 725)
                    this.cam.position.set(cam.position.x, tank.getY(), 0);

                if(tank.getIsHurt()) {
                    shake(5, 200);
                    tank.setIsHurt(false);
                }
            }
        }

        if(elapsed < duration) {
            float currentPower = intensity * this.cam.zoom * ((duration - elapsed) / duration);
            float x = (random.nextFloat() - 0.5f) * currentPower;
            float y = (random.nextFloat() - 0.5f) * currentPower;
            this.cam.translate(-x, -y);
            elapsed = (System.nanoTime() - shakeTimer) / 1000000;
        }
    }


    public void shake(float intensity, float duration) {
        this.elapsed = 0;
        this.shakeTimer = System.nanoTime();
        this.duration = duration;
        this.intensity = intensity;
    }
}
