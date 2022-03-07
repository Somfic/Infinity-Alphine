package alphine.components;

import alphine.ecs.Component;

public class TileComponent extends Component {
    private Texture texture;

    private int tileSetX;
    private int tileSetY;
    private final TileSet tileSet;

    public TileComponent(TileSet tileSet) {
        this.tileSet = tileSet;
    }

    public TileComponent setXY(int tileSetX, int tileSetY) {
        this.tileSetX = tileSetX;
        this.tileSetY = tileSetY;
        return this;
    }

    public int getTileSetX() {
        return tileSetX;
    }

    public int getTileSetY() {
        return tileSetY;
    }

    public Texture getTexture() {
        return tileSet.getTexture(tileSetX, tileSetY);
    }

    public TileSet getTileSet() {
        return tileSet;
    }
}

