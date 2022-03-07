package alphine.components;

import alphine.ecs.Component;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Vector3;

public class TransformComponent extends Component {
    private Vector3 position = new Vector3(0, 0, 0);
    private Vector2 scale = new Vector2(1, 1);
    private int z = 0;
    private double rotation = 0;
    private boolean isStatic = false;

    public Vector3 getPosition() {
        return position;
    }

    public TransformComponent setPosition(double x, double y) {
        this.position.x = x;
        this.position.y = y;

        return this;
    }

    public TransformComponent setPosition(double x, double y, double z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;

        return this;
    }

    public TransformComponent setPosition(Vector3 position) {
        this.position = position;

        return this;
    }

    public TransformComponent setPosition(double position) {
        this.position.x = position;
        this.position.y = position;
        this.position.z = 0;

        return this;
    }

    public TransformComponent setPositionX(double x) {
        this.position.x = x;

        return this;
    }

    public TransformComponent setPositionY(double y) {
        this.position.y = y;

        return this;
    }

    public TransformComponent setPositionZ(double z) {
        this.position.z = z;

        return this;
    }


    public double getPositionX() {
        return this.position.x;
    }

    public double getPositionY() {
        return this.position.y;
    }

    public double getPositionZ() {
        return this.position.z;
    }

    public Vector2 getScale() {
        return scale;
    }

    public TransformComponent setScale(Vector2 scale) {
        this.scale = scale;

        return this;
    }

    public TransformComponent setScale(double x, double y) {
        this.scale.x = x;
        this.scale.y = y;

        return this;
    }

    public TransformComponent setScale(double scale) {
        this.scale.x = scale;
        this.scale.y = scale;

        return this;
    }

    public TransformComponent setScaleX(double x) {
        this.scale.x = x;

        return this;
    }

    public TransformComponent setScaleY(double y) {
        this.scale.y = y;

        return this;
    }

    public double getScaleX() {
        return this.scale.x;
    }

    public double getScaleY() {
        return this.scale.y;
    }

    public double getRotation() {
        return rotation;
    }

    public TransformComponent setRotation(double degrees) {
        this.rotation = degrees;

        return this;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public TransformComponent setStatic(boolean isStatic) {
        this.isStatic = isStatic;

        return this;
    }
}
