package game.terrain;

import alphine.components.TileComponent;
import alphine.components.TileSet;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.System;
import game.maze.MazeComponent;
import game.noise.Noise;
import game.noise.layers.SimplexNoiseLayer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class TerrainSystem extends System {

    TileSet cubeSprites = new TileSet("256x256 Cubes.png", 256, 256);
    TileSet tileSprites = new TileSet("256x192 Tiles.png", 256, 192);

    Noise noise = new Noise();

    int width = 100;
    int height = 100;

    double minHeight = 0.5;
    boolean hasGenerated = false;

    List<TerrainLayer> layers = new ArrayList<>();

    Entity[][] blocks = new Entity[width][height];
    TransformComponent[][] transforms = new TransformComponent[width][height];
    TileComponent[][] tiles = new TileComponent[width][height];
    MazeComponent[][] mazes = new MazeComponent[width][height];
    double[][] heights = new double[width][height];

    ArrayList<Point> stack = new ArrayList<>();

    Point current;

    Point startPos = new Point(0, 0);
    Point endPos = new Point(0, 0);


    public TerrainSystem(Scene scene) {
        layers.add(new TerrainLayer(0.20, tileSprites, 3, 2));            // Deep water
        layers.add(new TerrainLayer(0.30, tileSprites, 1, 2));            // Shallow water
        layers.add(new TerrainLayer(0.4, tileSprites, 9, 3));            // Light sand
        layers.add(new TerrainLayer(0.5, cubeSprites, 9, 2));            // Dark sand
        layers.add(new TerrainLayer(0.60, cubeSprites, 5, 1));            // Grass
        layers.add(new TerrainLayer(0.70, cubeSprites, 5, 1)); // Grass
        layers.add(new TerrainLayer(0.85, cubeSprites, 6, 1));   // More grass
        layers.add(new TerrainLayer(0.90, cubeSprites, 4, 1)); // Grass with flowers
        layers.add(new TerrainLayer(1.00, cubeSprites, 3, 1));   // Snow

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                generate();
            }
        });
    }

    @Override
    public void onStart() {
        generate();
    }

    @Override
    public void onFixedUpdate(double dt) {
        if (hasGenerated) {
            return;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (mazes[x][y].isPath()) {

                    // Mark everything around it as a wall
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (i == 0 && j == 0) continue;
                            if (x + i < 0 || x + i >= width || y + j < 0 || y + j >= height) continue;
                            if (mazes[x + i][y + j].isPath()) continue;
                            if (mazes[x + i][y + j].isWall()) continue;

                            mazes[x + i][y + j].setWall(true);
                            tiles[x + i][y + j].setTileSet(cubeSprites).setXY(5, 6);
                            transforms[x + i][y + j].setPositionZ(0.75);
                        }
                    }

                    tiles[x][y].setTileSet(tileSprites).setXY(1, 8);
                    transforms[x][y].setPositionZ(0.5);
                }
            }
        }

        tiles[current.x][current.y].setTileSet(cubeSprites).setXY(1, 4);

        // Find unvisited neighbours
        List<Point> neighbours = new ArrayList<>();
        for (int x = -2; x <= 2; x += 2) {
            for (int y = -2; y <= 2; y += 2) {
                int xx = current.x + x;
                int yy = current.y + y;

                if (x == 0 && y == 0) continue;
                if (x != 0 && y != 0) continue;
                if (xx < 0 || xx >= width) continue;
                if (yy < 0 || yy >= height) continue;
                if (heights[xx][yy] < minHeight) continue;
                if (mazes[xx][yy].isPath()) continue;

                neighbours.add(new Point(xx, yy));
            }
        }

        // Pick a random neighbour
        if (neighbours.size() > 0) {
            int index = (int) (Math.random() * neighbours.size());
            Point next = neighbours.get(index);

            // Draw a straight line between the current and next
            double dx = next.x - current.x;
            double dy = next.y - current.y;

            if (dx == 0) {
                for (int y = Math.min(current.y, next.y); y <= Math.max(current.y, next.y); y++) {
                    mazes[current.x][y].setPath(true);
                }
            } else if (dy == 0) {
                for (int x = Math.min(current.x, next.x); x <= Math.max(current.x, next.x); x++) {
                    mazes[x][current.y].setPath(true);
                }
            }

            stack.add(current);

            current = next;
        } else {
            // No unvisited neighbours, backtrack
            if (stack.size() > 0) {
                current = stack.get(stack.size() - 1);
                stack.remove(stack.size() - 1);
            } else {
                hasGenerated = true;
            }
        }
    }

    void generate() {
        hasGenerated = false;

        noise.removeLayers();
        noise.addLayer(new SimplexNoiseLayer(60, 12, 0.5, 1.5).setSeed(java.lang.System.currentTimeMillis()));

        heights = noise.evaluate(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double value = heights[x][y];

                TerrainLayer layer = layers.get(0);
                for (TerrainLayer l : layers) {
                    if (value <= l.getThreshold()) {
                        layer = l;
                        break;
                    }
                }

                Entity block = new Entity("Terrain");
                TransformComponent transform = new TransformComponent().setPosition(x - width / 2f, y - height / 2f, layer.getOffset());
                TileComponent tile = new TileComponent().setTileSet(layer.getTileSet()).setXY(layer.getTileX(), layer.getTileY());
                MazeComponent maze = new MazeComponent();

                blocks[x][y] = block;
                transforms[x][y] = transform;
                tiles[x][y] = tile;
                mazes[x][y] = maze;

                block.addComponent(transform);
                block.addComponent(tile);
                addEntity(block);
            }
        }

        startPos = new Point(0, 0);
        endPos = new Point(0, 0);
        current = startPos;

        // Find a start position in the top middle of the map
        for (int y = 0; y < height; y++) {
            for (int x = width - 1; x >= 0; x--) {
                if (heights[x][y] > minHeight) {
                    startPos.setLocation(x, y);
                    break;
                }
            }

            if (startPos.x != 0) {
                break;
            }
        }

        // Find an end position in the bottom middle of the map
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (heights[x][y] > minHeight) {
                    endPos.setLocation(x, y);
                    break;
                }
            }

            if (endPos.x != 0) {
                break;
            }
        }

        current = startPos;
    }
}

