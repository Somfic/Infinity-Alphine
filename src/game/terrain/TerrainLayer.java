package game.terrain;

import alphine.components.TileSet;

public class TerrainLayer {
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
