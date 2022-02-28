package systems;

import components.FlatMaterial;
import components.Shape;
import components.Transform;
import ecs.World;
import ecs.System;
import ecs.Entity;
import ecs.EntityFilter;
import javafx.scene.canvas.Canvas;
import logging.Logger;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.util.List;

public class RenderSystem extends System {
    private final Canvas canvas;
    private FXGraphics2D graphics;

    private final EntityFilter filter;

    private final double pixelScale = 100;

    public RenderSystem(Canvas canvas) {
        this.canvas = canvas;
        this.filter = EntityFilter.create(Transform.class, Shape.class, FlatMaterial.class);
    }

    @Override
    public void onStart(World e) {
        graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
        graphics.scale(1f / pixelScale, 1f / pixelScale);
    }

    @Override
    public void onUpdate(double dt) {
        List<Entity> entities = getWorld().getEntities(this.filter);

        int width = (int)Math.ceil(canvas.getWidth() * pixelScale);
        int height = (int)Math.ceil(canvas.getHeight() * pixelScale);

        // todo: move this somewhere else? potential issue if this is done in the wrong order...
        // Clear the canvas
        graphics.setColor(Color.BLACK);
        graphics.clearRect(0, 0, width, height);

        width = (int)Math.ceil(canvas.getWidth() / 2f * pixelScale);
        height = (int)Math.ceil(canvas.getHeight() / 2f * pixelScale);

        // Translate to center of canvas
        graphics.translate(width, height);

        for (Entity entity : entities) {
            Transform transform = entity.getComponent(Transform.class);
            Shape shape = entity.getComponent(Shape.class);
            FlatMaterial material = entity.getComponent(FlatMaterial.class);

            // Translate to entity position
            // Round on two decimal places to avoid floating point errors
            int tx = (int)Math.round(transform.getPositionX() * pixelScale);
            int ty = (int)Math.round(transform.getPositionY() * pixelScale);
            int tr = (int)Math.toRadians(Math.round(transform.getRotation()));

            graphics.translate(tx, ty);
            graphics.rotate(tr);

            // Set color
            graphics.setColor(material.getFillColor());
            //todo: add other material properties

            switch (shape.getShape()) {
                case CIRCLE: {
                    int rx = (int) Math.round(transform.getScaleX() * pixelScale);
                    int ry = (int) Math.round(transform.getScaleY() * pixelScale);
                    int x = (int) Math.round(transform.getScaleX() / -2 * pixelScale);
                    int y = (int) Math.round(transform.getScaleX() / -2 * pixelScale);

                    if (material.isFilled())
                        graphics.fillOval(Math.round(x), Math.round(y), Math.round(rx), Math.round(ry));
                    else
                        graphics.drawOval(Math.round(x), Math.round(y), Math.round(rx), Math.round(ry));
                    break;
                }

                case RECTANGLE: {
                    int w = (int) Math.round(transform.getScaleX() * pixelScale);
                    int h = (int) Math.round(transform.getScaleY() * pixelScale);
                    int x = (int) Math.round(transform.getScaleX() * -pixelScale);
                    int y = (int) Math.round(transform.getScaleY() * -pixelScale);

                    if (material.isFilled())
                        graphics.fillRect(x, y, w, h);
                    else
                        graphics.drawRect(x, y, w, h);
                    break;
                }

                case TRIANGLE: {
                    int w = (int) Math.round(transform.getScaleX() * pixelScale);
                    int h = (int) Math.round(transform.getScaleY() * pixelScale);
                    int x = (int) Math.round(transform.getScaleX() * -pixelScale);
                    int y = (int) Math.round(transform.getScaleY() * -pixelScale);

                    if (material.isFilled())
                        graphics.fillPolygon(new int[]{x, x + w, x + w / 2}, new int[]{y, y + h, y + h / 2}, 3);
                    else
                        graphics.drawPolygon(new int[]{x, x + w, x + w / 2}, new int[]{y, y + h, y + h / 2}, 3);
                    break;
                }

                default:
                    Logger.error("Unknown shape type: " + shape.getShape());
                    break;
            }

            graphics.translate(-tx, -ty);
            graphics.rotate(-tr);
        }

        graphics.translate(-width, -height);

        graphics.setColor(Color.BLACK);
    }
}

