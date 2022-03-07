package alphine.systems;

import alphine.components.CameraComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.EntityFilter;
import alphine.ecs.System;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class CameraSystem extends System {

    EntityFilter filter = EntityFilter.create(TransformComponent.class, CameraComponent.class);

    public CameraSystem(Scene scene) {
        scene.setOnScroll(event -> {
            List<Entity> entities = getEntities(filter);
            for (Entity entity : entities) {
                CameraComponent camera = entity.getComponent(CameraComponent.class);
                camera.setZoom(camera.getZoom() + event.getDeltaY() * 0.1);
            }
        });
    }

    @Override
    public void onUpdate(double dt) {

    }
}
