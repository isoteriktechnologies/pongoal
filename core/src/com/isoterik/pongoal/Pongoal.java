package com.isoterik.pongoal;

import com.isoterik.pongoal.scenes.GameScene;
import com.isoterik.racken.GameDriver;
import com.isoterik.racken.Scene;
import com.isoterik.racken._2d.scenes.transition.SceneTransitions;

public class Pongoal extends GameDriver {
	@Override
	protected Scene initGame() {
		splashTransition = SceneTransitions.fade(1f);
		return new GameScene();
	}
}