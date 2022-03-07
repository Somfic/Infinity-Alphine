package alphine.systems;

import alphine.components.TileComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.EntityFilter;
import alphine.ecs.System;
import javafx.scene.canvas.GraphicsContext;
import org.dyn4j.geometry.Vector2;

import java.util.List;

public class IsometricRenderSystem extends System {

    private final EntityFilter filter = EntityFilter.create(TransformComponent.class, TileComponent.class);
    private final double tileSize = 28;

    @Override
    public void onRender(GraphicsContext graphics) {
        List<Entity> entities = getEntities(filter);

        for (Entity entity : entities) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            TileComponent tile = entity.getComponent(TileComponent.class);

            Vector2 isoPosition = getIsometricPosition(transform.getPosition());


        }
    }

    private Vector2 getIsometricPosition(double x, double y) {
        double isoX = x * 0.5f * tileSize + y * -0.5f * tileSize - tileSize / 2f;
        double isoY = x * 0.25f * tileSize + y * 0.25f * tileSize;

        return new Vector2(isoX, isoY);
    }

    private Vector2 getIsometricPosition(Vector2 position) {
        return getIsometricPosition(position.x, position.y);
    }
}
