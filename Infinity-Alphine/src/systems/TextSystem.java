package systems;

import components.Text;
import components.Transform;
import ecs.Entity;
import ecs.EntityFilter;
import ecs.System;
import ecs.World;
import javafx.scene.canvas.Canvas;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.util.List;

public class TextSystem extends System {

    private final EntityFilter filter;
    private final Canvas canvas;
    private FXGraphics2D graphics;

    public TextSystem(Canvas canvas) {
        this.filter = EntityFilter.create(Transform.class, Text.class);
        this.canvas = canvas;
    }

    @Override
    public void onStart(World e) {
        graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
    }

    @Override
    public void onRender() {
        List<Entity> entities = getWorld().getEntities(filter);

        int width = (int)Math.ceil(canvas.getWidth() / 2);
        int height = (int)Math.ceil(canvas.getHeight() / 2);

        // Translate to center of canvas
        graphics.translate(width, height);

        for (Entity entity : entities) {
            Transform transform = entity.getComponent(Transform.class);
            Text text = entity.getComponent(Text.class);

            int tx = (int)Math.ceil(transform.getPositionX());
            int ty = (int)Math.ceil(transform.getPositionY());

            // Translate to entity position
            graphics.translate(transform.getPositionX(), transform.getPositionY());

            // Rotate to entity rotation
            graphics.rotate(Math.toRadians(transform.getRotation()));

            if(transform.isStatic()) {
                // Reset center of canvas transform
                graphics.translate(-width, -height);
            }

            // Draw text
            Font font = text.getFont();
            FontMetrics metrics = graphics.getFontMetrics(font);
            graphics.setFont(text.getFont());
            graphics.setColor(text.getColor());
            graphics.drawString(text.getText(), 0, 0);

            if(transform.isStatic()) {
                // Reset the reset center of canvas transform
                graphics.translate(width, height);
            }

            // Reset rotation
            graphics.rotate(-Math.toRadians(transform.getRotation()));

            // Reset translation
            graphics.translate(-transform.getPositionX(), -transform.getPositionY());
        }

        graphics.setColor(Color.BLACK);

        // Translate to center of canvas
        graphics.translate(-width, -height);
    }
}
