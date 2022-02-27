package components;


import ecs.Component;
import org.dyn4j.geometry.Vector2;

public class RigidBody extends Component {
    private double mass = 1;
    private Vector2 acceleration = new Vector2(0, 0);
    private Vector2 velocity = new Vector2(0, 0);

    public double getMass() {
        return mass;
    }

    public RigidBody setMass(double mass) {
        this.mass = mass;

        return this;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public RigidBody setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;

        return this;
    }

    public RigidBody setAcceleration(double x, double y) {
        this.acceleration = new Vector2(x, y);

        return this;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public RigidBody setVelocity(Vector2 velocity) {
        this.velocity = velocity;

        return this;
    }

    public RigidBody setVelocity(double x, double y) {
        this.velocity = new Vector2(x, y);

        return this;
    }
}
