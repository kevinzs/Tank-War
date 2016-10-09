package com.kezarszy.tankwar.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.entities.Enemy;
import com.kezarszy.tankwar.entities.Player;
import com.kezarszy.tankwar.entities.Tank;
import com.kezarszy.tankwar.hud.HUD;
import com.kezarszy.tankwar.levels.Level;
import com.kezarszy.tankwar.sounds.Sounds;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.badlogic.gdx.math.MathUtils.random;

public class PlayState extends State {

    private Player player;
    private HashMap<String,Tank> tanks;
    private Level level;

    private HUD hud;

    private Sounds sounds;

    private Socket socket;

    private float duration;
    private float intensity;
    private float elapsed;
    private float shakeTimer;

    public PlayState(GameStateManager gsm){
        super(gsm);

        level = new Level(this.viewport);

        tanks = new HashMap<String, Tank>();
        player = new Player(100,100);
        player.setLevel(level);
        connectSocket();
        configSocketEvents();

        hud = new HUD();
        hud.setPlayer(player);

        sounds = new Sounds();
        sounds.loadMusic();
        //sounds.playGameMusic();

        for(Tank tank: tanks.values())
            tank.setLevel(level);
    }

    public void update(){
        cam.update();
        player.update(tanks);
        if(!player.getIsAlive())
            player.setHealth(-50);
        for(Tank tank: tanks.values()) {
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
        player.draw(sb);
        for(Tank tank: tanks.values())
            tank.draw(sb);
        hud.render();
    }

    public void updateCam(){
        if(player != null) {
            if ((player.getX() > 480 && player.getX() < 520) && (player.getY() > 270 && player.getY() < 725))
                this.cam.position.set(player.getX(), player.getY(), 0);
            if (player.getX() > 480 && player.getX() < 520)
                this.cam.position.set(player.getX(), cam.position.y, 0);
            if (player.getY() > 270 && player.getY() < 725)
                this.cam.position.set(cam.position.x, player.getY(), 0);

            if (player.getIsHurt()) {
                shake(5, 200);
                player.setIsHurt(false);
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

    public void connectSocket(){
        try{
            socket = IO.socket("http://localhost:8080").connect();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO","Connected to the server.");
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "New player connect: " + id);
                    tanks.put(id, new Enemy(300,150));
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting new player ID");
                }
            }
        });
    }
}
