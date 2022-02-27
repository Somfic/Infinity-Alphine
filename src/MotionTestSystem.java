import components.FlatMaterial;
import components.RigidBody;
import components.Shape;
import components.Transform;
import ecs.Entity;
import ecs.EntityFilter;
import ecs.System;
import ecs.World;
import input.InputManager;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MotionTestSystem extends System {
    EntityFilter filter = EntityFilter.create(RigidBody.class, Transform.class);

    @Override
    public void onStart(World e) {
        for (int i = 0; i < 10; i++) {
            Entity object = new Entity("Test object");
            object.addComponent(new RigidBody().setVelocity(new Random().nextDouble() * 50 - 50, new Random().nextDouble() * 50 - 50));
            object.addComponent(new Transform().setPosition(new Random().nextDouble() * 200 - 200, new Random().nextDouble() * 200 - 200).setScale(20));
            object.addComponent(new Shape(Shape.PrimitiveShape.RECTANGLE));
            object.addComponent(new FlatMaterial().setFillColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255))));
            e.addEntity(object);
        }
    }

    @Override
    public void onUpdate(double dt) {
        Vector2 mouse = new Vector2(0, 0);

        List<Entity> entites = getWorld().getEntities(filter);

        for (Entity entity : entites) {
            Vector2 position = entity.getComponent(Transform.class).getPosition();
            entity.getComponent(RigidBody.class).setAcceleration(mouse.subtract(position));
        }
    }
}
