package com.isoterik.pongoal.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Racken;
import com.isoterik.racken.animation.FrameAnimation;
import com.isoterik.racken.physics2d.Physics2d;
import com.isoterik.racken.physics2d.PhysicsManager2d;
import com.isoterik.racken.physics2d.PhysicsMaterial2d;
import com.isoterik.racken.physics2d.RigidBody2d;
import com.isoterik.racken.physics2d.colliders.BoxCollider;

public class Pud extends Physics2d {
    public enum PudPosition {
        Top, Bottom
    }

    private final PudPosition pudPosition;
    private final FrameAnimation animation;

    private final Array<TextureRegion> textureRegions;

    private final RigidBody2d rigidBody;
    private final PhysicsManager2d physicsManager;

    private float velocity;
    private final float leftBound, rightBound;

    public Pud(PudPosition pudPosition, GameObject pudObject, PhysicsManager2d physicsManager, float leftBound,
               float rightBound) {
        this.pudPosition = pudPosition;
        this.physicsManager = physicsManager;
        this.leftBound = leftBound;
        this.rightBound = rightBound;

        TextureAtlas pudAtlas = Racken.instance().assets.getAtlas("puds.atlas");

        String prefix = pudPosition == PudPosition.Top ? "pud_blue_" : "pud_red_";
        textureRegions = new Array<>();

        for (int i = 1; i < 6; i++) {
            textureRegions.add(pudAtlas.findRegion(prefix + i));
        }

        animation = new FrameAnimation(textureRegions, .05f);
        pudObject.addComponent(animation);
        animation.setPlayMode(Animation.PlayMode.REVERSED);

        PhysicsMaterial2d physicsMaterial = new PhysicsMaterial2d();
        rigidBody = new RigidBody2d(BodyDef.BodyType.KinematicBody, physicsMaterial, physicsManager);
        pudObject.addComponent(rigidBody);
        pudObject.addComponent(new BoxCollider());

        pudObject.addComponent(this);

        stopAnimation();
    }

    public void startAnimation() {
        animation.setStateTime(0);
        animation.setEnabled(true);
    }

    public void stopAnimation() {
        animation.setEnabled(false);
    }

    private void pushLeft(float speed, Body body) {
        velocity -= speed;
        body.setLinearVelocity(velocity, 0);
    }

    private void pushRight(float speed, Body body) {
        velocity += speed;
        body.setLinearVelocity(velocity, 0);
    }

    private void stopMovement(Body body) {
        velocity = 0;
        body.setLinearVelocity(velocity, 0);
    }

    private void keepWithinBounds(Body body) {
        float width = gameObject.transform.getWidth();
        Vector2 position = body.getPosition();
        position.sub(width * .5f, 0);

        if (position.x < leftBound)
            position.x = leftBound;
        if ((position.x + width) > rightBound)
            position.x = rightBound - width;

        position.add(width * .5f, 0);
        body.setTransform(position, body.getAngle());
    }

    @Override
    public void fixedUpdate2d(float timeStep) {
        if (pudPosition == PudPosition.Bottom) {
            float movementForce = .2f;

            Body body = rigidBody.getBody();

            if (input.isKeyDown(Input.Keys.LEFT) || input.isKeyDown(Input.Keys.A))
                pushLeft(movementForce, body);
            else if (input.isKeyDown(Input.Keys.RIGHT) || input.isKeyDown(Input.Keys.D))
                pushRight(movementForce, body);
            else
                stopMovement(body);

            keepWithinBounds(body);
        }
    }
}


















