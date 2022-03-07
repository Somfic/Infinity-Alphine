import alphine.components.*;
import alphine.ecs.Entity;
import alphine.ecs.System;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class TestSystem extends System {

    List<Entity> entities = new ArrayList<>();

    TileSet cubes = new TileSet("256x256 Cubes.png", 256, 256);
    TileSet tiles = new TileSet("256x192 Tiles.png", 256, 192);
    TileSet trees = new TileSet("256x512 Trees.png", 256, 512);

    OpenSimplexNoise noise = new OpenSimplexNoise(java.lang.System.currentTimeMillis());
    OpenSimplexNoise treeNoise = new OpenSimplexNoise(java.lang.System.currentTimeMillis() + 1);

    double scale = 0.03;
    int amount = 300;
    private Entity camera;

    @Override
    public void onStart() {
        camera = new Entity("Camera");
        camera.addComponent(new TransformComponent());
        camera.addComponent(new CameraComponent().setZoom(20));
        addEntity(camera);

        for (int x = -amount; x <= amount; x++) {
            for (int y = -amount; y <= amount; y++) {
                Entity entity = new Entity("Tile test");
                entity.addComponent(new TransformComponent().setPosition(x, y));
                entity.addComponent(new TileComponent());

                TransformComponent transform = entity.getComponent(TransformComponent.class);
                TileComponent tile = entity.getComponent(TileComponent.class);

                int tileX = 0;
                int tileY = 0;

                TileSet tileSet;
                double height = noise.eval(x * scale, y * scale);

                if (height < -0.3) {
                    // Deep water
                    tileSet = tiles;
                    tileX = 2;
                    tileY = 1;
                } else if (height < 0) {
                    // Shallow water
                    tileSet = tiles;
                    tileX = 0;
                    tileY = 1;
                } else if (height < 0.2) {
                    // Shore
                    tileSet = tiles;
                    tileX = (int) Math.round(height * 2f + 2);
                    tileY = 2;
                } else if (height < 0.3) {
                    // Sand
                    tileSet = tiles;
                    tileX = (int) Math.round(height * 2f + 2);
                    tileY = 0;
                } else if (height < 0.6) {
                    // Grass
                    tileSet = cubes;
                    tileX = (int) Math.round(height * 2f + 2);
                    tileY = 0;
                } else {
                    // Mountain
                    tileSet = cubes;
                    tileX = (int) Math.round(height * 9f);
                    tileY = 0;
                }

                tile.setTileSet(tileSet).setXY(tileX, tileY);
                addEntity(entity);
                entities.add(entity);
            }
        }

    }

    double time;

    @Override
    public void onFixedUpdate(double dt) {
        time += dt;

        TransformComponent cameraTransform = camera.getComponent(TransformComponent.class);
        cameraTransform.setPositionX(time * 50);
    }
}