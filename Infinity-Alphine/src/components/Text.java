package components;

import ecs.Component;

import java.awt.*;

public class Text extends Component {
    private String text;
    private Font font;
    private Color color;

    public Text() {
        this.text = "";
        this.color = Color.RED;
        this.font = new Font("Monospaced", Font.PLAIN, 12);
    }

    public String getText() {
        return text;
    }

    public Text setText(String text) {
        this.text = text;

        return this;
    }

    public Font getFont() {
        return font;
    }

    public Text setFont(Font font) {
        this.font = font;
        return this;
    }

    public Text setFontFamily(String fontFamily) {
        this.font = new Font(fontFamily, this.font.getStyle(), this.font.getSize());
        return this;
    }

    public Text setFontStyle(int fontStyle) {
        this.font = new Font(this.font.getFamily(), fontStyle, this.font.getSize());
        return this;
    }

    public Text setFontSize(int fontSize) {
        this.font = new Font(this.font.getFamily(), this.font.getStyle(), fontSize);
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Text setColor(Color color) {
        this.color = color;

        return this;
    }
}
