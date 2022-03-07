package alphine.systems;

import alphine.components.*;
import alphine.ecs.Entity;
import alphine.ecs.EntityFilter;
import alphine.ecs.System;
import alphine.logging.Logger;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Vector3;

import java.util.List;

public class IsometricRenderSystem extends System {

    private Image textureSet;

    private final EntityFilter filter = EntityFilter.create(TransformComponent.class, TileComponent.class);
    private final EntityFilter cameraFilter = EntityFilter.create(TransformComponent.class, CameraComponent.class);

    private Vector3 cameraPosition;

    private double tileSize = 20;
    private final Canvas canvas;

    public IsometricRenderSystem(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void onRender(GraphicsContext graphics) {
        List<Entity> cameras = getEntities(cameraFilter);
        if(cameras.size() == 0) {
            Logger.warn("No camera found!");
            return;
        }

        Entity cameraEntity = cameras.get(0);
        TransformComponent cameraTransform = cameraEntity.getComponent(TransformComponent.class);
        cameraPosition = cameraTransform.getPosition();

        CameraComponent camera = cameraEntity.getComponent(CameraComponent.class);
        tileSize = camera.getZoom();

        List<Entity> entities = getEntities(filter);

        for (Entity entity : entities) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            Vector2 isoPosition = getIsometricPosition(transform.getPosition());

            double padding = tileSize;
            if(isoPosition.x < -padding || isoPosition.y < -padding || isoPosition.x > canvas.getWidth() + padding || isoPosition.y > canvas.getHeight() + padding) {
                continue;
            }

            TileComponent tile = entity.getComponent(TileComponent.class);
            tile.getTileSet().loadTextures();
            Texture texture = tile.getTexture();

            Image image = texture.getImage();
            double aspectRatio = tileSize / image.getWidth();
            double height = image.getHeight() * aspectRatio;

            graphics.drawImage(texture.getImage(), isoPosition.x, isoPosition.y + (tileSize - height), tileSize, height);
        }
    }

    private Vector2 getIsometricPosition(double x, double y, double z) {
        double isoX = x * 0.5f * tileSize + y * -0.5f * tileSize - tileSize / 2f + canvas.getWidth() / 2f - cameraPosition.x;
        double isoY = x * 0.25f * tileSize + y * 0.25f * tileSize + z * tileSize / 2f + canvas.getHeight() / 2f - cameraPosition.y;

        return new Vector2(isoX, isoY);
    }

    private Vector2 getIsometricPosition(Vector3 position) {
        return getIsometricPosition(position.x, position.y, position.z);
    }
}
