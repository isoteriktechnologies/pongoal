package com.isoterik.pongoal.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.Component;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Racken;
import com.isoterik.racken.animation.FrameAnimation;
import com.isoterik.racken.input.IKeyListener;
import com.isoterik.racken.input.KeyEventData;
import com.isoterik.racken.input.KeyTrigger;
import com.isoterik.racken.physics2d.PhysicsManager2d;
import com.isoterik.racken.physics2d.PhysicsMaterial2d;
import com.isoterik.racken.physics2d.RigidBody2d;
import com.isoterik.racken.physics2d.colliders.BoxCollider;

public class Pud extends Component {
    public enum PudPosition {
        Top, Bottom
    }

    private final PudPosition pudPosition;
    private final FrameAnimation animation;

    private final Array<TextureRegion> textureRegions;

    private final RigidBody2d rigidBody;
    private final PhysicsManager2d physicsManager;

    private float velocity;

    public Pud(PudPosition pudPosition, GameObject pudObject, PhysicsManager2d physicsManager) {
        this.pudPosition = pudPosition;
        this.physicsManager = physicsManager;

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

    private void pushLeft(float force) {
        Body body = rigidBody.getBody();
        velocity -= force;
        body.setLinearVelocity(velocity, 0);
    }

    private void pushRight(float force) {
        Body body = rigidBody.getBody();
        velocity += force;
        body.setLinearVelocity(velocity, 0);
    }

    @Override
    public void start() {
        Body body = rigidBody.getBody();
        body.setLinearVelocity(velocity, 0);

        if (pudPosition == PudPosition.Bottom) {
            float movementForce = .1f;

            String mappingMoveLeft = "MOVE_PUD_LEFT_";
            String mappingMoveRight = "MOVE_PUD_RIGHT_";

            input.addMapping(mappingMoveLeft, KeyTrigger.keyDownTrigger(Input.Keys.LEFT).setPolled(true),
                    KeyTrigger.keyDownTrigger(Input.Keys.A).setPolled(true));
            input.addMapping(mappingMoveRight, KeyTrigger.keyDownTrigger(Input.Keys.RIGHT).setPolled(true),
                    KeyTrigger.keyDownTrigger(Input.Keys.D).setPolled(true));

            input.mapListener(mappingMoveLeft, (IKeyListener) (mappingName, keyEventData) -> pushLeft(movementForce));
            input.mapListener(mappingMoveRight, (IKeyListener) (mappingName, keyEventData) -> pushRight(movementForce));
        }
    }
}


















