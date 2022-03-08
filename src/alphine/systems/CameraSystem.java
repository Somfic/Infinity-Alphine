package alphine.systems;

import alphine.components.CameraComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.EntityFilter;
import alphine.ecs.System;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import org.dyn4j.geometry.Vector3;

import java.util.List;

public class CameraSystem extends System {

    EntityFilter filter = EntityFilter.create(TransformComponent.class, CameraComponent.class);

    private ScrollEvent scrollEventHandler;
    private KeyEvent keyEventHandler;

    public CameraSystem(Scene scene) {
        scene.setOnScroll(event -> {
            scrollEventHandler = event;
        });

        scene.setOnKeyPressed(event -> {
            keyEventHandler = event;
        });
    }

    @Override
    public void onFixedUpdate(double dt) {
        List<Entity> entities = getEntities(filter);
        if (entities.size() == 0) return;

        Entity entity = entities.get(0);

        if (scrollEventHandler != null) {
            handleScroll(entity, scrollEventHandler, dt);
            scrollEventHandler = null;
        }

        if (keyEventHandler != null) {
            handleKey(entity, keyEventHandler, dt);
            keyEventHandler = null;
        }
    }

    private void handleScroll(Entity entity, ScrollEvent event, double dt) {
        CameraComponent camera = entity.getComponent(CameraComponent.class);
        camera.setZoom(camera.getZoom() + event.getDeltaY() * 0.1);
    }

    private void handleKey(Entity entity, KeyEvent event, double dt) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        Vector3 delta = new Vector3();

        if (event.getCode() == KeyCode.W) {
            delta.y = -1;
        } else if (event.getCode() == KeyCode.S) {
            delta.y = 1;
        } else if (event.getCode() == KeyCode.A) {
            delta.x = -1;
        } else if (event.getCode() == KeyCode.D) {
            delta.x = 1;
        }

        transform.setPosition(transform.getPosition().add(delta).multiply(dt * 50));
    }
}
