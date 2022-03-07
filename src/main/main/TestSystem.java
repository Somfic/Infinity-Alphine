import alphine.components.*;
import alphine.ecs.Entity;
import alphine.ecs.System;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class TestSystem extends System {

    List<Entity> entities = new ArrayList<>();

    @Override
    public void onStart() {

        TileSet cubes = new TileSet("256x256 Cubes.png", 256, 256);
        TileSet tiles = new TileSet("256x192 Tiles.png", 256, 192);
        TileSet trees = new TileSet("256x512 Trees.png", 256, 512);

        OpenSimplexNoise noise = new OpenSimplexNoise(java.lang.System.currentTimeMillis());
        OpenSimplexNoise treeNoise = new OpenSimplexNoise(java.lang.System.currentTimeMillis() + 1);

        double scale = 0.07;
        int amount = 100;


        for (int x = -amount; x <= amount; x++) {
            for (int y = -amount; y <= amount; y++) {
                int tileX = 0;
                int tileY = 0;

                TileSet tileSet;
                double height = noise.eval(x * scale, y * scale);

                if (height < -0.3) {
                    // Deep water
                    tileSet = tiles;
                    tileX = 2;
                    tileY = 1;
                }

                else if (height < 0) {
                    // Shallow water
                    tileSet = tiles;
                    tileX = 0;
                    tileY = 1;
                }

                else if (height < 0.2) {
                    // Shore
                    tileSet = tiles;
                    tileX = (int)Math.round(Math.random() * 2f + 2);
                    tileY = 2;
                }

                else if (height < 0.3) {
                    // Sand
                    tileSet = tiles;
                    tileX = (int)Math.round(Math.random() * 2f + 2);
                    tileY = 0;
                }

                else if (height < 0.6) {
                    // Grass
                    tileSet = cubes;
                    tileX = (int)Math.round(Math.random()* 2f + 2);
                    tileY = 0;
                }

                else {
                    // Mountain
                    tileSet = cubes;
                    tileX = (int)Math.round(Math.random() * 9f);
                    tileY = 0;

                    if(treeNoise.eval(x * scale, y * scale) > 0.2 && Math.random() > 0.5) {
                       // Tree
                       Entity tree = new Entity("Tree");
                       tree.addComponent(new TransformComponent().setPosition(x, y, -1));
                       tree.addComponent(new TileComponent(trees).setXY(3, 0));
                       addEntity(tree);
                    }
                }

                Entity tile = new Entity("Tile test");
                tile.addComponent(new TransformComponent().setPosition(x, y));
                tile.addComponent(new TileComponent(tileSet).setXY(tileX, tileY));

                addEntity(tile);
                entities.add(tile);
            }
        }
    }
}