package components;

import ecs.Component;

public class Shape extends Component {
    private PrimitiveShape shape;

    public Shape(PrimitiveShape shape) {
        this.shape = shape;
    }

    public PrimitiveShape getShape() {
        return shape;
    }

    public void setShape(PrimitiveShape shape) {
        this.shape = shape;
    }

   public enum PrimitiveShape {
        RECTANGLE,
        CIRCLE,
        TRIANGLE,
        LINE
    }

}

