package com.kezarszy.tankwar.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

    private Sound shoot;
    private Sound game_music;
    private Sound explosion;

    public Sounds(){}

    public void loadMusic(){
        game_music = Gdx.audio.newSound(Gdx.files.internal("sounds/battleThemeA.mp3"));
    }

    public void loadTankSounds(){
        shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/missile_explosion.ogg"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explode.wav"));
    }

    public void playGameMusic(){
        long id = game_music.play(0.75f);
        game_music.setLooping(id, true);
    }

    public void playShootSound(){
        long id = shoot.play(1.0f);
    }

    public void playExplosionSound(){
        long id = explosion.play(1.0f);
    }
}
