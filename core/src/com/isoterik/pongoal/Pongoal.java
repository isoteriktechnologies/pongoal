package com.isoterik.pongoal;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.isoterik.pongoal.scenes.GameScene;
import com.isoterik.racken.GameDriver;
import com.isoterik.racken.Scene;
import com.isoterik.racken._2d.scenes.transition.SceneTransitions;

public class Pongoal extends GameDriver {
	@Override
	protected Scene initGame() {
		loadAssets();

		racken.defaultSettings.VIEWPORT_WIDTH = 720f;
		racken.defaultSettings.VIEWPORT_HEIGHT = 960f;
		racken.defaultSettings.PIXELS_PER_UNIT = 48f;

		splashTransition = SceneTransitions.fade(1f);
		return new GameScene();
	}

	private void loadAssets() {
		racken.assets.enqueueAsset("map/map.tmx", TiledMap.class);
		racken.assets.enqueueTexture("goal_posts_lights.png");
		racken.assets.loadAssetsNow();
	}
}