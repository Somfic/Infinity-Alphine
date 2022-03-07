package alphine.components;

import alphine.ecs.Component;

public class CameraComponent extends Component {
    private double zoom = 1f;

    public CameraComponent setZoom(double zoom) {
        this.zoom = Math.max(0.0001, zoom);
        return this;
    }

    public double getZoom() {
        return zoom;
    }
}
