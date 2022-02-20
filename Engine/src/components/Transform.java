package components;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec3d;
import ecs.components.Component;

import java.awt.font.TransformAttribute;

public class Transform implements Component {
    private Vec3d position;
    private Vec2d scale;
    private double rotation;

    public Transform() {
        this.position = new Vec3d(0, 0, 0);
        this.scale = new Vec2d(1, 1);
        this.rotation = 0;
    }

    public Vec3d getPosition() {
        return position;
    }

    public Transform setPosition(double x, double y, double z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;

        return this;
    }

    public Transform setPositionX(double x) {
        this.position.x = x;

        return this;
    }

    public Transform setPositionY(double y) {
        this.position.y = y;

        return this;
    }

    public Transform setPositionZ(double z) {
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

    public Vec2d getScale() {
        return scale;
    }

    public Transform setScale(double x, double y) {
        this.scale.x = x;
        this.scale.y = y;

        return this;
    }

    public Transform setScaleX(double x) {
        this.scale.x = x;

        return this;
    }

    public Transform setScaleY(double y) {
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

    public Transform setRotation(double degrees) {
        this.rotation = degrees;

        return this;
    }
}
