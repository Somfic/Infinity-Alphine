package systems;

import components.RigidBody;
import components.Shape;
import components.Transform;
import ecs.Entity;
import ecs.EntityFilter;
import ecs.System;
import ecs.World;
import logging.Logger;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.Force;
import org.dyn4j.geometry.*;

import java.util.HashMap;
import java.util.List;

public class MotionSystem extends System {

    private final EntityFilter filter = EntityFilter.create(Transform.class, RigidBody.class, Shape.class);
    private final org.dyn4j.world.World<Body> simulation = new org.dyn4j.world.World<>();

    @Override
    public void onStart(World e) {
        simulation.setGravity(new Vector2(0, 0));
    }

    private final HashMap<Entity, Body> bodies = new HashMap<>();

    @Override
    public void onUpdate(double dt) {
        List<Entity> entities = super.getWorld().getEntities(filter);
        for (Entity entity : entities) {

            Shape shape = entity.getComponent(Shape.class);
            Transform transform = entity.getComponent(Transform.class);
            RigidBody rigidBody = entity.getComponent(RigidBody.class);

            // Add the entity to the world if it doesn't already exist
            if (!bodies.containsKey(entity)) {
                Body body = new Body();
                BodyFixture fixture;

                switch (shape.getShape()) {
                    case CIRCLE:
                        fixture = new BodyFixture(new Ellipse(transform.getScaleX(), transform.getScaleY()));
                        break;

                    case RECTANGLE:
                        fixture = new BodyFixture(new org.dyn4j.geometry.Rectangle(transform.getScaleX(), transform.getScaleY()));
                        break;

                    default:
                        Logger.error("Unsupported shape type: " + shape.getShape());
                        continue;
                }

                body.addFixture(fixture);

                body.getTransform().setTranslation(transform.getPosition());
                body.getTransform().setRotation(transform.getRotation());
                body.setMass(MassType.NORMAL);

                simulation.addBody(body);

                bodies.put(entity, body);
            }

            Body body = bodies.get(entity);

            // Update the rigidbody
            transform.setPosition(transform.getPosition().add(body.getChangeInPosition()));

            // Make sure the position is within the bounds of the world
            int bounds = 1000;
            transform.setPositionX(Math.max(-bounds, Math.min(transform.getPositionX(), bounds)));
            transform.setPositionY(Math.max(-bounds, Math.min(transform.getPositionY(), bounds)));

            transform.setRotation(body.getTransform().getRotationAngle());
        }
    }

    double time = 0;

    @Override
    public void onFixedUpdate(double dt) {
        time += dt;

         for (Entity entity : bodies.keySet()) {
            RigidBody rigidBody = entity.getComponent(RigidBody.class);
            Transform transform = entity.getComponent(Transform.class);
            Body body = bodies.get(entity);

            Vector2 force = new Vector2(Math.sin(time) * 100, Math.cos(time) * 100).subtract(transform.getPosition()).multiply(50);
            body.applyForce(force);
        }


        simulation.update(dt);
    }
}
