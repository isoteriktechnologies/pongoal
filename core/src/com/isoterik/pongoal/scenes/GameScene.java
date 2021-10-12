package com.isoterik.pongoal.scenes;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.isoterik.pongoal.components.PostLightManager;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Scene;
import com.isoterik.racken._2d.components.renderer.TiledMapRenderer;

public class GameScene extends Scene {
    private final GameObject gameManager;

    private GameObject topPost, bottomPost;
    private GameObject topPostLightLeft, topPostLightRight, bottomPostLightLeft, bottomPostLightRight;
    private PostLightManager topPostLightManager, bottomPostLightManager;

    private final TiledMap map;
    private final TiledMapRenderer mapRenderer;

    public GameScene() {
        gameManager = GameObject.newInstance("GameManager");
        addGameObject(gameManager);

        map = racken.assets.getAsset("map/map.tmx", TiledMap.class);
        mapRenderer = new TiledMapRenderer(map, 1/48f);
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

        topPostLightManager = new PostLightManager(topPostLightLeft, topPostLightRight,
                PostLightManager.PostPosition.Top, gameWorldUnits);
        bottomPostLightManager = new PostLightManager(bottomPostLightLeft, bottomPostLightRight,
                PostLightManager.PostPosition.Bottom, gameWorldUnits);
        gameManager.addComponent(topPostLightManager);
        gameManager.addComponent(bottomPostLightManager);
    }
}






























