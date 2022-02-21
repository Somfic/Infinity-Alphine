import components.FlatMaterial;
import components.Shape;
import components.Transform;
import ecs.Entity;
import ecs.System;
import ecs.World;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Locale;

public class TestSystem extends System {

    Entity objectCounter;

    LocalDateTime started;

    @Override
    public void onStart(World e) {
        objectCounter = new Entity("ObjectCounter");
        objectCounter.addComponent(new components.Text().setColor(Color.cyan));
        objectCounter.addComponent(new Transform().setPosition(10, 35, 0).setStatic(true));
        getWorld().addEntity(objectCounter);

        started = LocalDateTime.now();
    }

    long counter = 0;

    @Override
    public void onUpdate(double dt) {
        if (LocalDateTime.now().isBefore(started.plusSeconds(5))) {
            return;
        }

        for (int i = 0; i < dt * 10000; i++) {
            counter++;

            Entity entity = new Entity("Henlo");

            // Random position
            entity.addComponent(new Transform().setScale(Math.random() * 10, Math.random() * 10).setPosition(Math.random() * 500 - 250, Math.random() * 500 - 250, 0).setRotation(Math.random() * 360));
            entity.addComponent(new FlatMaterial().setFillColor(Color.getHSBColor((float) Math.random(), (float) Math.random(),1f)).setFilled(false));
            entity.addComponent(new components.Shape(Shape.PrimitiveShape.RECTANGLE));

            getWorld().addEntity(entity);
        }

        // Update the counter
        objectCounter.getComponent(components.Text.class).setText(String.format(Locale.ENGLISH, "%,d", counter) + " objects");
    }
}
