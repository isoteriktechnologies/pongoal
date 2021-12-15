package com.isoterik.pongoal.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.isoterik.pongoal.Pongoal;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		int preferredWidth = 860;
		int preferredHeight = 1280;
		int width = preferredWidth;
		int height = preferredHeight;

		Dimension size = Toolkit. getDefaultToolkit().getScreenSize();
		if (size.width < preferredWidth)
			width = size.width;

		if (size.height < preferredHeight)
			height = size.height;

		config.setWindowedMode(width, height);
		new Lwjgl3Application(new Pongoal(), config);
	}
}
