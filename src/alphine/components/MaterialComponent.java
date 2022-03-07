package alphine.components;

import alphine.ecs.Component;
import javafx.scene.paint.Color;

public class MaterialComponent extends Component {
    private Color fill;
    private Color stroke;
    private double strokeWidth;

    public MaterialComponent setFill(Color fill) {
        this.fill = fill;
        return this;
    }

    public Color getFill() {
        return fill;
    }

    public MaterialComponent setStroke(Color stroke) {
        this.stroke = stroke;
        return this;
    }

    public Color getStroke() {
        return stroke;
    }

    public MaterialComponent setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }
}

