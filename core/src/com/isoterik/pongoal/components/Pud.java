package com.isoterik.pongoal.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.Component;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Racken;
import com.isoterik.racken.animation.FrameAnimation;

public class Pud extends Component {
    public enum PudPosition {
        Top, Bottom
    }

    private final PudPosition pudPosition;
    private final FrameAnimation animation;

    private final Array<TextureRegion> textureRegions;

    public Pud(PudPosition pudPosition, GameObject pudObject) {
        this.pudPosition = pudPosition;

        TextureAtlas pudAtlas = Racken.instance().assets.getAtlas("puds.atlas");

        String prefix = pudPosition == PudPosition.Top ? "pud_blue_" : "pud_red_";
        textureRegions = new Array<>();

        for (int i = 1; i < 6; i++) {
            textureRegions.add(pudAtlas.findRegion(prefix + i));
        }

        animation = new FrameAnimation(textureRegions, .05f);
        pudObject.addComponent(animation);
        pudObject.addComponent(this);
        animation.setPlayMode(Animation.PlayMode.REVERSED);

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


















