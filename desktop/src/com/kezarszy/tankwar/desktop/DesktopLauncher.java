package com.kezarszy.tankwar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kezarszy.tankwar.Game;
import com.kezarszy.tankwar.TankWar;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TankWar.WIDTH;
		config.height = TankWar.HEIGHT;
		new LwjglApplication(new TankWar(), config);
	}
}
