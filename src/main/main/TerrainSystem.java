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

    int width = 200;
    int height = 400;

    public TerrainSystem() {
        noise.addLayer(new SimplexNoiseLayer(50, 8, 0.6, 1.6).setSeed(java.lang.System.currentTimeMillis()));

        layers.add(new TerrainLayer(0.30, tiles, 3, 2));            // Deep water
        layers.add(new TerrainLayer(0.40, tiles, 1, 2));            // Shallow water
        layers.add(new TerrainLayer(0.45, tiles, 9, 3));            // Light sand
        layers.add(new TerrainLayer(0.50, cubes, 9, 2));            // Dark sand
        layers.add(new TerrainLayer(0.60, cubes, 5, 1));            // Grass
        layers.add(new TerrainLayer(0.70, cubes, 5, 1, 0.5)); // Grass
        layers.add(new TerrainLayer(0.85, cubes, 6, 1, 1));   // More grass
        layers.add(new TerrainLayer(0.90, cubes, 4, 1, 1.5)); // Grass with flowers
        layers.add(new TerrainLayer(1.00, cubes, 3, 1, 2));   // Snow
    }

    @Override
    public void onStart() {
        double[][] noiseMap = noise.evaluate(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
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
                block.addComponent(new TransformComponent().setPosition(x - width / 2f, y - height / 2f, layer.getOffset()));
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
    private final double offset;

    public TerrainLayer(double threshold, TileSet tileSet, int tileX, int tileY) {
        this.tileSet = tileSet;
        this.tileX = tileX;
        this.tileY = tileY;
        this.threshold = threshold;
        this.offset = 0;
    }

    public TerrainLayer(double threshold, TileSet tileSet, int tileX, int tileY, double offset) {
        this.tileSet = tileSet;
        this.tileX = tileX;
        this.tileY = tileY;
        this.threshold = threshold;
        this.offset = offset;
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

    public double getOffset() {
        return offset;
    }
}