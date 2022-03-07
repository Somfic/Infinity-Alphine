package alphine.components;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Texture {
    private final Image image;

    public Texture(Image texture, int x, int y, int width, int height) {
        PixelReader reader = texture.getPixelReader();
        image = new WritableImage(reader, x, y, width, height);
    }

    public Texture(Image texture) {
        this.image = texture;
    }

    public Image getImage() {
        return image;
    }
}
