package alphine.systems;

import alphine.components.CameraComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.EntityFilter;
import alphine.ecs.System;
import alphine.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import org.dyn4j.geometry.Vector3;

import java.util.HashMap;
import java.util.List;

public class CameraSystem extends System {

    EntityFilter filter = EntityFilter.create(TransformComponent.class, CameraComponent.class);

    private ScrollEvent scrollEventHandler;

    private KeyEvent keyPressedEvent;
    private KeyEvent keyReleasedEvent;

    public CameraSystem(Scene scene) {
        scene.setOnScroll(event -> {
            scrollEventHandler = event;
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            keyPressedEvent = event;
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            keyReleasedEvent = event;
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

        if (keyPressedEvent != null) {
            handleKey(entity, keyPressedEvent, true, dt);
            keyPressedEvent = null;
        }

        if (keyReleasedEvent != null) {
            handleKey(entity, keyReleasedEvent, false, dt);
            keyReleasedEvent = null;
        }
    }

    private void handleScroll(Entity entity, ScrollEvent event, double dt) {
        CameraComponent camera = entity.getComponent(CameraComponent.class);

        double oldZoom = camera.getZoom();
        double newZoom = oldZoom + (1f / event.getDeltaY() * oldZoom * 1.5);
        if (newZoom < 5) newZoom = 5;
        if (newZoom > 200) newZoom = 200;

        camera.setZoom(newZoom);
        Logger.debug("Camera zoom: " + camera.getZoom());
    }

    private final HashMap<KeyCode, Boolean> keys = new HashMap<>();

    private void handleKey(Entity entity, KeyEvent event, boolean isDown, double dt) {
        keys.put(event.getCode(), isDown);

        TransformComponent transform = entity.getComponent(TransformComponent.class);
        CameraComponent camera = entity.getComponent(CameraComponent.class);

        Vector3 delta = new Vector3();

        if (keys.getOrDefault(KeyCode.W, false)) {
            delta.y = -1;
        }

        if ((keys.getOrDefault(KeyCode.S, false))) {
            delta.y = 1;
        }

        if ((keys.getOrDefault(KeyCode.A, false))) {
            delta.x = -1;
        }

        if ((keys.getOrDefault(KeyCode.D, false))) {
            delta.x = 1;
        }

        transform.setPosition(transform.getPosition().add(delta.multiply(100f * dt * (1f / camera.getZoom()))));
    }
}
