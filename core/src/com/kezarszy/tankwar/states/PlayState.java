package com.kezarszy.tankwar.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kezarszy.tankwar.entities.Bullet;
import com.kezarszy.tankwar.entities.Enemy;
import com.kezarszy.tankwar.entities.Player;
import com.kezarszy.tankwar.entities.Tank;
import com.kezarszy.tankwar.hud.HUD;
import com.kezarszy.tankwar.levels.Level;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.badlogic.gdx.math.MathUtils.random;

public class PlayState extends State {

    private final float UPDATE_TIME = 1/60f;
    private float timer;

    private HashMap<String,Tank> tanks;
    private Level level;

    private HUD hud;

    private Socket socket;

    private float duration;
    private float intensity;
    private float elapsed;
    private float shakeTimer;

    private String playerID;

    private State thisState;

    public PlayState(GameStateManager gsm, AssetManager manager){
        super(gsm, manager);
        thisState = this;

        level = new Level(this.viewport);

        tanks = new HashMap<String, Tank>();

        connectSocket();
        configSocketEvents();

        hud = new HUD();

        /*Sound game_music = manager.get("sounds/battleThemeA.mp3", Sound.class);
        long id = game_music.play(0.75f);
        game_music.setLooping(id, true);*/

        for(Tank tank: tanks.values())
            tank.setLevel(level);
    }

    @Override
    public void update(){
        cam.update();
        if(tanks.get(playerID) != null) { // Only do this if the player is alive
            tanks.get(playerID).update(tanks);
            if (!tanks.get(playerID).getIsAlive())
                tanks.get(playerID).setHealth(-50);
            for (Tank tank : tanks.values()) {
                if (tank != tanks.get(playerID)) {
                    tank.update(tanks);
                    if (!tank.getIsAlive()) {
                        tanks.remove(tank.getID());
                        break;
                    }
                }
            }
        }
        updateCam();
        updateServer(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        if(tanks.get(playerID) != null) {
            level.render(sb);
            tanks.get(playerID).draw(sb);
            for (Tank tank : tanks.values())
                tank.draw(sb);
            hud.render();
        }
    }

    public void updateCam(){
        if(tanks.get(playerID) != null) {
            if ((tanks.get(playerID).getX() > 480 && tanks.get(playerID).getX() < 520)
                    && (tanks.get(playerID).getY() > 270 && tanks.get(playerID).getY() < 725))
                this.cam.position.set(tanks.get(playerID).getX(), tanks.get(playerID).getY(), 0);
            if (tanks.get(playerID).getX() > 480 && tanks.get(playerID).getX() < 520)
                this.cam.position.set(tanks.get(playerID).getX(), cam.position.y, 0);
            if (tanks.get(playerID).getY() > 270 && tanks.get(playerID).getY() < 725)
                this.cam.position.set(cam.position.x, tanks.get(playerID).getY(), 0);

            if (tanks.get(playerID).getIsHurt()) {
                shake(5, 200);
                tanks.get(playerID).setIsHurt(false);
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

    public void updateServer(float dt){
        timer += dt;
        Player player = (Player) tanks.get(playerID);
        if(timer >= UPDATE_TIME && player != null){
            if(player.hasMoved()) {
                JSONObject data = new JSONObject();
                try {
                    data.put("x", player.getX());
                    data.put("y", player.getY());
                    data.put("rotation", player.getRotation());
                    socket.emit("playerMoved", data);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error sendind update data");
                }
            }
            if(player.isShooting()){
                JSONObject data = new JSONObject();
                try {
                    data.put("shooting", true);
                    socket.emit("playerShooting",data);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error sendind shooting data");
                }
            }
        }
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
                    int x = data.getJSONObject("respawn").getInt("x");
                    int y = data.getJSONObject("respawn").getInt("y");
                    int rotation = data.getJSONObject("respawn").getInt("rotation");
                    Gdx.app.log("SocketIO", "My ID: " + id);
                    tanks.put(id, new Player(x,y,thisState));
                    playerID = id;
                    tanks.get(playerID).setLevel(level);
                    tanks.get(playerID).setID(id);
                    hud.setPlayer((Player) tanks.get(playerID));

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
                    int x = data.getJSONObject("respawn").getInt("x");
                    int y = data.getJSONObject("respawn").getInt("y");
                    int rotation = data.getJSONObject("respawn").getInt("rotation");
                    Gdx.app.log("SocketIO", "New player connect: " + id);
                    tanks.put(id, new Enemy(x,y, thisState));
                    tanks.get(id).setLevel(level);
                    tanks.get(id).setID(id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting new player ID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "Player disconnected: " + id);
                    tanks.remove(id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting disconnected player");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    int x = data.getInt("x");
                    int y = data.getInt("y");
                    int angle = data.getInt("rotation");
                    if(tanks.get(id) != null){
                        tanks.get(id).setPosition(x,y);
                        tanks.get(id).setRotation(angle);
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting player movement.");
                }
            }
        }).on("playerShooting", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    boolean shooting = data.getBoolean("shooting");
                    if(tanks.get(id) != null){
                        tanks.get(id).shooting(shooting);
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting player shooting.");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray data = (JSONArray) args[0];
                try {
                    for(int i=0; i<data.length(); i++){
                        int x = ((Double) data.getJSONObject(i).getDouble("x")).intValue();
                        int y = ((Double) data.getJSONObject(i).getDouble("y")).intValue();
                        int angle = ((Double) data.getJSONObject(i).getDouble("rotation")).intValue();
                        Enemy enemy = new Enemy(x,y,thisState);
                        tanks.put(data.getJSONObject(i).getString("id"), enemy);
                        tanks.get(data.getJSONObject(i).getString("id")).setLevel(level);
                        tanks.get(data.getJSONObject(i).getString("id")).setRotation(angle);
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting disconnected player");
                }
            }
        });
    }

    @Override
    public void dispose() {
        tanks.get(playerID).dispose();
        for(Tank tank: tanks.values())
            tank.dispose();
    }
}
