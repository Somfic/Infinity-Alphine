package alphine.components;

import alphine.logging.Logger;
import javafx.scene.image.Image;

import java.io.InputStream;

public class TileSet {
    private Texture[][] map;

    private final String texture;
    private final int tileWidth;
    private final int tileHeight;

    private boolean isLoaded = false;

    public TileSet(String texture, int tileWidth, int tileHeight) {
        this.texture = texture;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public void loadTextures() {
        if (isLoaded) {
            return;
        }

        Logger.debug("Loading textures for tileset " + this.texture);

        InputStream textureStream = getClass().getClassLoader().getResourceAsStream(texture);

        if (textureStream == null) {
            throw new Error("Texture not found");
        }

        Image image = new Image(textureStream);

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        this.map = new Texture[width / tileWidth][height / tileHeight];

        for (int x = 0; x < width / tileWidth; x++) {
            for (int y = 0; y < height / tileHeight; y++) {
                this.map[x][y] = new Texture(image, x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            }
        }

        isLoaded = true;
    }

    public Texture getTexture(int x, int y) {
        if(x <= 0 || y <= 0 || x > map.length || y > map[0].length) {
            Logger.warn("Tile out of bounds (" + x + ", " + y + ") for tileset '" + this.texture + "'");
            return null;
        }

        return this.map[x - 1][y - 1];
    }
}
