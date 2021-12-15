package com.isoterik.pongoal.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.Component;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Racken;
import com.isoterik.racken.animation.FrameAnimation;
import com.isoterik.racken.physics2d.PhysicsManager2d;
import com.isoterik.racken.physics2d.PhysicsMaterial2d;
import com.isoterik.racken.physics2d.RigidBody2d;
import com.isoterik.racken.physics2d.colliders.CircleCollider;

public class Ball extends Component {
    private final Array<TextureRegion> textureRegions;
    private final FrameAnimation animation;

    private final RigidBody2d rigidBody;

    public Ball(GameObject ballObject, PhysicsManager2d physicsManager) {
        textureRegions = new Array<>();

        Texture ballTexture = Racken.instance().assets.getTexture("ball_frames.png");
        TextureRegion[] regions = TextureRegion.split(ballTexture, 48, 48)[0];
        textureRegions.addAll(regions);

        animation = new FrameAnimation(textureRegions, .05f);

        ballObject.addComponent(animation);
        ballObject.addComponent(this);

        PhysicsMaterial2d physicsMaterial = new PhysicsMaterial2d();
        physicsMaterial.bounciness = .9f;
        rigidBody = new RigidBody2d(BodyDef.BodyType.DynamicBody, physicsMaterial, physicsManager);
        ballObject.addComponent(rigidBody);
        ballObject.addComponent(new CircleCollider());

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
