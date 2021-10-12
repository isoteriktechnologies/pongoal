package com.isoterik.pongoal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.Component;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Racken;
import com.isoterik.racken._2d.components.renderer.SpriteRenderer;
import com.isoterik.racken.animation.FrameAnimation;
import com.isoterik.racken.utils.GameWorldUnits;

public class PostLightManager extends Component {
    private GameObject leftLight, rightLight;
    private PostPosition postPosition;

    private Array<TextureRegion> topLights, bottomLights;
    private FrameAnimation leftAnimation, rightAnimation;

    public enum PostPosition {
        Top, Bottom
    }

    public PostLightManager(GameObject leftLight, GameObject rightLight, PostPosition postPosition, GameWorldUnits gameWorldUnits) {
        this.leftLight = leftLight;
        this.rightLight = rightLight;
        this.postPosition = postPosition;

        topLights = new Array<>();
        bottomLights = new Array<>();

        Texture lightTexture = Racken.instance().assets.getTexture("goal_posts_lights.png");
        TextureRegion[][] lights = TextureRegion.split(lightTexture, 64, 64);

        for (int i = 0; i < lights[0].length; i ++)
            topLights.add(lights[0][i]);

        for (int i = 0; i < lights[3].length; i++)
            bottomLights.add(lights[3][i]);

        Array<TextureRegion> animationFrames = topLights;
        if (postPosition == PostPosition.Bottom)
            animationFrames = bottomLights;

        leftAnimation = new FrameAnimation(animationFrames, .05f);
        rightAnimation = new FrameAnimation(animationFrames, .05f);
        leftLight.addComponent(new SpriteRenderer(animationFrames.first(), gameWorldUnits));
        leftLight.addComponent(leftAnimation);
        rightLight.addComponent(new SpriteRenderer(animationFrames.first(), gameWorldUnits));
        rightLight.addComponent(rightAnimation);
    }
}



















