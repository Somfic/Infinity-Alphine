package alphine.components;

import alphine.ecs.Component;
import javafx.scene.paint.Color;

public class TextComponent extends Component {
    public String content;
    public int fontSize;
    public String font;

    public TextComponent setContent(String content) {
        this.content = content;
        return this;
    }

    public TextComponent setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TextComponent setFont(String font) {
        this.font = font;
        return this;
    }

    public String getContent() {
        return content;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getFont() {
        return font;
    }
}
