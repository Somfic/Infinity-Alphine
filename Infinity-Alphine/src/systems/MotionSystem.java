package systems;

import components.RigidBody;
import components.Transform;
import ecs.Entity;
import ecs.EntityFilter;
import ecs.System;
import logging.Logger;

import java.util.List;

public class MotionSystem extends System {
    private final EntityFilter filter = EntityFilter.create(Transform.class, RigidBody.class);

    @Override
    public void onUpdate(double dt) {
        List<Entity> entities = getWorld().getEntities(filter);

        for (Entity entity : entities) {
            Transform transform = entity.getComponent(Transform.class);
            RigidBody rigidBody = entity.getComponent(RigidBody.class);

            rigidBody.setVelocity(rigidBody.getVelocity().add(rigidBody.getAcceleration().multiply(dt)));
            transform.setPosition(transform.getPosition().add(rigidBody.getVelocity().multiply(dt)));
        }
    }
}
