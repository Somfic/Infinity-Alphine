package components;

import ecs.Component;

public class Shape extends Component {
    private PrimitiveShape shape = PrimitiveShape.RECTANGLE;

    public PrimitiveShape getShape() {
        return shape;
    }

    public Shape setShape(PrimitiveShape shape) {
        this.shape = shape;

        return this;
    }

   public enum PrimitiveShape {
        RECTANGLE,
        CIRCLE,
        TRIANGLE,
        LINE
    }

}

