package components;


import ecs.Component;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class RigidBody extends Component {
    private Body body = new Body();

    public Body getBody() {
        return body;
    }

    public double getMass() {
        return body.getMass().getMass();
    }

    public RigidBody setMass(double mass, double inertia) {
        body.setMass(new Mass(new Vector2(), mass, inertia));
        body.setMassType(MassType.NORMAL);

        return this;
    }
}
