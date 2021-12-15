package com.isoterik.pongoal.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.isoterik.pongoal.components.Ball;
import com.isoterik.pongoal.components.PostLight;
import com.isoterik.pongoal.components.Pud;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Scene;
import com.isoterik.racken._2d.components.renderer.TiledMapRenderer;

public class GameScene extends Scene {
    private final GameObject gameManager;

    private Pud topPud, bottomPud;

    private Ball ball;

    private GameObject topPost, bottomPost;
    private GameObject topPostLightLeft, topPostLightRight, bottomPostLightLeft, bottomPostLightRight;

    private PostLight topPostLight, bottomPostLight;

    private final TiledMap map;
    private final TiledMapRenderer mapRenderer;

    public GameScene() {
        setBackgroundColor(Color.BLACK);

        gameManager = GameObject.newInstance("GameManager");
        addGameObject(gameManager);

        map = racken.assets.getAsset("map/map.tmx", TiledMap.class);
        mapRenderer = new TiledMapRenderer(map, 1/64f);
        gameManager.addComponent(mapRenderer);

        init();
    }

    private void init() {
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

            if (properties.get("name").equals("post")) {
                if (properties.get("position").equals("top"))
                    topPost = gameObject;
                else
                    bottomPost = gameObject;
            }
            else if (properties.get("name").equals("pud_down")) {
                bottomPud = new Pud(Pud.PudPosition.Bottom, gameObject);
            }
            else if (properties.get("name").equals("pud_top")) {
                topPud = new Pud(Pud.PudPosition.Top, gameObject);
            }
            else if (properties.get("name").equals("ball")) {
                ball = new Ball(gameObject);
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

        Array<RectangleMapObject> rectangleObjects = mapRenderer.getRectangleObjects();
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
        }

        topPostLight = new PostLight(topPostLightLeft, topPostLightRight,
                PostLight.PostPosition.Top, gameWorldUnits);
        bottomPostLight = new PostLight(bottomPostLightLeft, bottomPostLightRight,
                PostLight.PostPosition.Bottom, gameWorldUnits);
        gameManager.addComponent(topPostLight);
        gameManager.addComponent(bottomPostLight);
    }
}






























