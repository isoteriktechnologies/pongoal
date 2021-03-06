package com.isoterik.pongoal.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.isoterik.pongoal.components.Ball;
import com.isoterik.pongoal.components.PostLight;
import com.isoterik.pongoal.components.Pud;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Scene;
import com.isoterik.racken._2d.components.renderer.TiledMapRenderer;
import com.isoterik.racken.physics2d.PhysicsManager2d;
import com.isoterik.racken.physics2d.PhysicsMaterial2d;
import com.isoterik.racken.physics2d.RigidBody2d;
import com.isoterik.racken.physics2d.colliders.BoxCollider;

public class GameScene extends Scene {
    private final GameObject gameManager;
    private GameObject topPost, bottomPost;
    private GameObject topPostLightLeft, topPostLightRight, bottomPostLightLeft, bottomPostLightRight;
    private GameObject topGoalLine, bottomGoalLine;
    private GameObject leftWall, rightWall;

    private Pud topPud, bottomPud;

    private Ball ball;

    private PostLight topPostLight, bottomPostLight;

    private final TiledMap map;
    private final TiledMapRenderer mapRenderer;

    private PhysicsManager2d physicsManager;

    public GameScene() {
        setBackgroundColor(Color.BLACK);

        gameManager = GameObject.newInstance("GameManager");
        addGameObject(gameManager);

        map = racken.assets.getAsset("map/map.tmx", TiledMap.class);
        mapRenderer = new TiledMapRenderer(map, 1/64f);
        gameManager.addComponent(mapRenderer);

        init();
        startGame();
    }

    private void init() {
        mainCamera.setup(new FitViewport(gameWorldUnits.getWorldWidth(), gameWorldUnits.getWorldHeight()));

        physicsManager = PhysicsManager2d.setup(this, new Vector2(0, -3));
        //physicsManager.setRenderPhysicsDebugLines(true);
        physicsManager.setSimulatePhysics(false);

        Array<RectangleMapObject> rectangleObjects = mapRenderer.getRectangleObjects();
        PhysicsMaterial2d wallMaterial = new PhysicsMaterial2d();
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            MapProperties properties = rectangleObject.getProperties();
            float width = gameWorldUnits.toWorldUnit((float)properties.get("width"));
            float height = gameWorldUnits.toWorldUnit((float)properties.get("height"));
            float x = gameWorldUnits.toWorldUnit((float)properties.get("x"));
            float y = gameWorldUnits.toWorldUnit((float)properties.get("y"));

            GameObject rect = GameObject.newInstance();
            rect.transform.setSize(width, height);
            rect.transform.setPosition(x, y);
            addGameObject(rect);

            BoxCollider collider = new BoxCollider();

            String name = properties.get("name", String.class);
            if (name != null && name.equals("goal_line")) {
                collider.setIsSensor(true);

                if (properties.get("position").equals("top"))
                    topGoalLine = rect;
                else
                    bottomGoalLine = rect;
            }

            if (name != null && name.equals("wall")) {
                if (properties.get("position").equals("left"))
                    leftWall = rect;
                else
                    rightWall = rect;
            }

            rect.addComponent(new RigidBody2d(BodyDef.BodyType.StaticBody, wallMaterial, physicsManager));
            rect.addComponent(collider);
        }

        Array<TiledMapTileMapObject> tileObjects = mapRenderer.getTileObjects();
        for (TiledMapTileMapObject tileObject : tileObjects) {
            MapProperties properties = tileObject.getProperties();
            float width = gameWorldUnits.toWorldUnit((float)properties.get("width"));
            float height = gameWorldUnits.toWorldUnit((float)properties.get("height"));
            float x = gameWorldUnits.toWorldUnit((float)properties.get("x"));
            float y = gameWorldUnits.toWorldUnit((float)properties.get("y"));

            tileObject.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                    Texture.TextureFilter.Linear);
            GameObject gameObject = newSpriteObject(tileObject.getTextureRegion());
            gameObject.transform.setSize(width, height);
            gameObject.transform.setPosition(x, y);
            addGameObject(gameObject);

            float leftBound = leftWall.transform.getX() + leftWall.transform.getWidth();
            float rightBound = rightWall.transform.getX();

            if (properties.get("name").equals("post")) {
                if (properties.get("position").equals("top"))
                    topPost = gameObject;
                else
                    bottomPost = gameObject;
            }
            else if (properties.get("name").equals("pud_bottom")) {
                bottomPud = new Pud(Pud.PudPosition.Bottom, gameObject, physicsManager, leftBound, rightBound);
            }
            else if (properties.get("name").equals("pud_top")) {
                topPud = new Pud(Pud.PudPosition.Top, gameObject, physicsManager, leftBound, rightBound);
            }
            else if (properties.get("name").equals("ball")) {
                ball = new Ball(gameObject, physicsManager);
            }
        }

        Array<EllipseMapObject> ellipseObjects = mapRenderer.getEllipseObjects();
        for (EllipseMapObject ellipseObject : ellipseObjects) {
            MapProperties properties = ellipseObject.getProperties();
            float width = gameWorldUnits.toWorldUnit((float)properties.get("width"));
            float height = gameWorldUnits.toWorldUnit((float)properties.get("height"));
            float x = gameWorldUnits.toWorldUnit((float)properties.get("x"));
            float y = gameWorldUnits.toWorldUnit((float)properties.get("y"));

            GameObject light = GameObject.newInstance("Light");
            light.transform.setSize(width, height);
            light.transform.setPosition(x, y);
            addGameObject(light);

            if (properties.get("name").equals("top_post_light_left"))
                topPostLightLeft = light;
            else if (properties.get("name").equals("top_post_light_right"))
                topPostLightRight = light;
            else if (properties.get("name").equals("bottom_post_light_left"))
                bottomPostLightLeft = light;
            else if (properties.get("name").equals("bottom_post_light_right"))
                bottomPostLightRight = light;
        }

        topPostLight = new PostLight(topPostLightLeft, topPostLightRight,
                PostLight.PostPosition.Top, gameWorldUnits);
        bottomPostLight = new PostLight(bottomPostLightLeft, bottomPostLightRight,
                PostLight.PostPosition.Bottom, gameWorldUnits);
        gameManager.addComponent(topPostLight);
        gameManager.addComponent(bottomPostLight);
    }

    private void startGame() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ball.bounceRandomly();
                physicsManager.setSimulatePhysics(true);
            }
        }, 3);
    }
}






























