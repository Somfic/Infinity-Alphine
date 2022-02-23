package components;

public class RigidBody {
    private double mass;

    public RigidBody(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public RigidBody setMass(double mass) {
        this.mass = mass;

        return this;
    }
}
