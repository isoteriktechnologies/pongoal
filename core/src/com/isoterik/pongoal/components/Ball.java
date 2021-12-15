package com.isoterik.pongoal.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.Component;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Racken;
import com.isoterik.racken.animation.FrameAnimation;

public class Ball extends Component {
    private final Array<TextureRegion> textureRegions;
    private final FrameAnimation animation;

    public Ball(GameObject ballObject) {
        textureRegions = new Array<>();

        Texture ballTexture = Racken.instance().assets.getTexture("ball_frames.png");
        TextureRegion[] regions = TextureRegion.split(ballTexture, 48, 48)[0];
        textureRegions.addAll(regions);

        animation = new FrameAnimation(textureRegions, .05f);

        ballObject.addComponent(animation);
        ballObject.addComponent(this);

        stopAnimation();
    }

    public void startAnimation() {
        animation.setStateTime(0);
        animation.setEnabled(true);
    }

    public void stopAnimation() {
        animation.setEnabled(false);
    }
}
