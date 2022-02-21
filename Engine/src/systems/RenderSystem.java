package systems;

import components.FlatMaterial;
import components.Shape;
import components.Transform;
import ecs.World;
import ecs.EngineSystem;
import ecs.Entity;
import ecs.EntityFamily;
import javafx.scene.canvas.Canvas;
import logging.Logger;
import org.jfree.fx.FXGraphics2D;

import java.util.List;

public class RenderSystem extends EngineSystem {
    private final Canvas canvas;
    private FXGraphics2D graphics;

    private EntityFamily filter;

    public RenderSystem(Canvas canvas) {
        this.canvas = canvas;
        this.filter = EntityFamily.create(Transform.class, Shape.class, FlatMaterial.class);
    }

    @Override
    public void onStart(World e) {
        graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
    }

    @Override
    public void onUpdate(double dt) {
        List<Entity> entities = getEngine().getEntities(this.filter);

        int width = (int)Math.ceil(canvas.getWidth());
        int height = (int)Math.ceil(canvas.getHeight());

        // Clear the canvas
        graphics.clearRect(0, 0, width, height);

        // Translate to center of canvas
        graphics.translate(width / 2, height / 2);

        for (Entity entity : entities) {
            Transform transform = entity.getComponent(Transform.class);
            Shape shape = entity.getComponent(Shape.class);
            FlatMaterial material = entity.getComponent(FlatMaterial.class);

            // Translate to entity position
            graphics.translate(transform.getPosition().x, transform.getPosition().y);

            // Rotate to entity rotation
            graphics.rotate(Math.toRadians(transform.getRotation()));

            // Set color
            graphics.setColor(material.getFillColor());
            //todo: add other material properties

            switch (shape.getShape()) {
                case CIRCLE: {
                    int rx = (int) Math.round(transform.getScaleX());
                    int ry = (int) Math.round(transform.getScaleY());
                    int x = (int) Math.round(transform.getScaleX() / -2.0);
                    int y = (int) Math.round(transform.getScaleX() / -2.0);

                    if (material.isFilled())
                        graphics.fillOval(Math.round(x), Math.round(y), Math.round(rx), Math.round(ry));
                    else
                        graphics.drawOval(Math.round(x), Math.round(y), Math.round(rx), Math.round(ry));
                    break;
                }

                case RECTANGLE: {
                    int w = (int) Math.round(transform.getScaleX());
                    int h = (int) Math.round(transform.getScaleY());
                    int x = (int) Math.round(transform.getScaleX() / 2.0);
                    int y = (int) Math.round(transform.getScaleY() / 2.0);

                    if (material.isFilled())
                        graphics.fillRect(x, y, w, h);
                    else
                        graphics.drawRect(x, y, w, h);
                    break;
                }

                case TRIANGLE: {
                    int w = (int) Math.round(transform.getScaleX());
                    int h = (int) Math.round(transform.getScaleY());
                    int x = (int) Math.round(transform.getScaleX() / 2.0);
                    int y = (int) Math.round(transform.getScaleY() / 2.0);

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

            // Reset rotation
            graphics.rotate(-Math.toRadians(transform.getRotation()));

            // Reset translation
            graphics.translate(-transform.getPosition().x, -transform.getPosition().y);
        }

        // Translate back from center of canvas
        graphics.translate(-width / 2, -height / 2);
    }
}

