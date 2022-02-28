import components.FlatMaterial;
import components.RigidBody;
import components.Shape;
import components.Transform;
import ecs.Entity;
import ecs.System;
import ecs.World;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class ObjectTest extends System {

    Entity objectCounter;

    LocalDateTime started;

    @Override
    public void onStart(World e) {
        objectCounter = new Entity("ObjectCounter");
        objectCounter.addComponent(new components.Text().setColor(Color.cyan));
        objectCounter.addComponent(new Transform().setPosition(10, 35).setStatic(true));
        getWorld().addEntity(objectCounter);

        started = LocalDateTime.now();

        start = LocalDateTime.now();
    }

    LocalDateTime start;



    int count = 0;

    @Override
    public void onUpdate(double dt) {
        if(LocalDateTime.now().isBefore(start.plusSeconds(5))) {
            return;
        }

        for (int i = 0; i < dt * 2; i++) {
            if(count > 500)
                return;

            Entity entity = new Entity("Henlo");

            float size = (float) Math.random() * 30 + 10;

            // Random position
            entity.addComponent(new FlatMaterial().setFillColor(Color.getHSBColor((float) Math.random(), (float) Math.random(),1f)).setFilled(true));
            entity.addComponent(new Transform().setScale(size).setPosition(Math.random() * 2000 - 1000, Math.random() * 2000 - 1000).setRotation(Math.random() * 360));
            entity.addComponent(new Shape().setShape(Shape.PrimitiveShape.CIRCLE));
            entity.addComponent(new RigidBody().setMass(200, 1));

            count++;
            getWorld().addEntity(entity);
        }

        // Update the counter
        objectCounter.getComponent(components.Text.class).setText(String.format(Locale.ENGLISH, "%,d", count) + " objects");
    }
}
