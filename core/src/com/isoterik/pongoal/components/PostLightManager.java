package com.isoterik.pongoal.components;

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

    private Array<TextureRegion> leftLights, rightLights;
    private FrameAnimation leftAnimation, rightAnimation;

    public enum PostPosition {
        Top, Bottom
    }

    public PostLightManager(GameObject leftLight, GameObject rightLight, PostPosition postPosition, GameWorldUnits gameWorldUnits) {
        this.leftLight = leftLight;
        this.rightLight = rightLight;
        this.postPosition = postPosition;

        leftLights = new Array<>();
        rightLights = new Array<>();

        Texture lightTexture = Racken.instance().assets.getTexture("goal_posts_lights.png");
        TextureRegion[][] lights = TextureRegion.split(lightTexture, 64, 64);

        if (postPosition == PostPosition.Top) {
            for (int i = 0; i < lights[0].length; i ++)
                leftLights.add(lights[0][i]);

            for (int i = 0; i < lights[2].length; i++)
                rightLights.add(lights[2][i]);
        } else {
            for (int i = 0; i < lights[1].length; i ++)
                leftLights.add(lights[1][i]);

            for (int i = 0; i < lights[3].length; i++)
                rightLights.add(lights[3][i]);
        }

        leftAnimation = new FrameAnimation(leftLights, .05f);
        rightAnimation = new FrameAnimation(rightLights, .05f);
        leftLight.addComponent(new SpriteRenderer(leftLights.first(), gameWorldUnits));
        leftLight.addComponent(leftAnimation);
        rightLight.addComponent(new SpriteRenderer(rightLights.first(), gameWorldUnits));
        rightLight.addComponent(rightAnimation);
    }
}



















