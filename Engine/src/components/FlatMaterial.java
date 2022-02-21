package components;

import ecs.Component;

import java.awt.*;

public class FlatMaterial extends Component {

    private Color fillColor;
    private Color strokeColor;

    private boolean isFilled;

    public Color getFillColor() {
        return fillColor;
    }

    public FlatMaterial setFillColor(Color fillColor) {
        this.fillColor = fillColor;

        return this;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public FlatMaterial setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;

        return this;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public FlatMaterial setFilled(boolean filled) {
        isFilled = filled;

        return this;
    }
}

