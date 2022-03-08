import alphine.components.CameraComponent;
import alphine.components.TileComponent;
import alphine.components.TileSet;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.System;
import noise.Noise;
import noise.layers.SimplexNoiseLayer;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TerrainSystem extends System {

    List<Entity> entities = new ArrayList<>();

    TileSet cubes = new TileSet("256x256 Cubes.png", 256, 256);
    TileSet tiles = new TileSet("256x192 Tiles.png", 256, 192);
    TileSet trees = new TileSet("256x512 Trees.png", 256, 512);

    Noise noise = new Noise();

    List<TerrainLayer> layers = new ArrayList<>();
    HashMap<Vector2, Entity> blocks = new HashMap<>();

    int size = 300;

    public TerrainSystem() {
        noise.addLayer(new SimplexNoiseLayer(40, 8, 0.6, 1.5).setSeed(java.lang.System.currentTimeMillis()));

        layers.add(new TerrainLayer(0.30, tiles, 3, 2)); // Deep water
        layers.add(new TerrainLayer(0.40, tiles, 1, 2)); // Shallow water
        layers.add(new TerrainLayer(0.45, tiles, 9, 3)); // Sand
        layers.add(new TerrainLayer(0.5, cubes, 3, 2)); // Grass
        layers.add(new TerrainLayer(0.60, cubes, 5, 1)); // Grass 2
        layers.add(new TerrainLayer(0.85, cubes, 6, 1)); // Rock
        layers.add(new TerrainLayer(0.90, cubes, 3, 4)); // Rock 2
        layers.add(new TerrainLayer(1.00, cubes, 1, 3)); // Snow
    }

    @Override
    public void onStart() {
        Entity camera = new Entity("Camera");
        camera.addComponent(new TransformComponent());
        camera.addComponent(new CameraComponent().setZoom(20));
        addEntity(camera);

        double[][] noiseMap = noise.evaluate(size, size);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                double value = noiseMap[x][y];

                TerrainLayer layer = layers.get(0);
                for (TerrainLayer l : layers) {
                    if (value <= l.getThreshold()) {
                        layer = l;
                        break;
                    }
                }

                // Check if we already have a block here
                if (blocks.containsKey(new Vector2(x, y))) {
                    continue;
                }

                Entity block = new Entity("Block");
                block.addComponent(new TransformComponent().setPosition(x - size / 2f, y - size / 2f, value > 0.41 ? Math.floor(2f * ((value - 0.4) * -5f)) / 2f : 0));
                block.addComponent(new TileComponent().setTileSet(layer.getTileSet()).setXY(layer.getTileX(), layer.getTileY()));
                addEntity(block);

                blocks.put(new Vector2(x, y), block);
            }
        }
    }

    double time;

    @Override
    public void onFixedUpdate(double dt) {

    }
}

class TerrainLayer {
    private final TileSet tileSet;
    private final int tileX;
    private final int tileY;
    private final double threshold;

    public TerrainLayer(double threshold, TileSet tileSet, int tileX, int tileY) {
        this.tileSet = tileSet;
        this.tileX = tileX;
        this.tileY = tileY;
        this.threshold = threshold;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public int getTileX() {
        return tileX;
    }

    public double getThreshold() {
        return threshold;
    }

    public int getTileY() {
        return tileY;
    }
}