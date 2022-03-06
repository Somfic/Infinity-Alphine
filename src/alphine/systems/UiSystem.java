package alphine.systems;

import alphine.components.MaterialComponent;
import alphine.components.TextComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.EntityFilter;
import alphine.ecs.System;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import alphine.logging.Logger;

import java.util.List;

public class UiSystem extends System {

    private final EntityFilter filter = EntityFilter.create(TransformComponent.class, TextComponent.class, MaterialComponent.class);

    @Override
    public void onRender(GraphicsContext graphics) {
        List<Entity> entities = getEntities(filter);

        for (Entity entity : entities) {
            Logger.debug("Rendering entity");

            TransformComponent transform = entity.getComponent(TransformComponent.class);
            TextComponent text = entity.getComponent(TextComponent.class);
            MaterialComponent material = entity.getComponent(MaterialComponent.class);

            graphics.save();

            graphics.translate(transform.getPositionX(), transform.getPositionY());

            graphics.setFont(Font.font(text.getFont(), FontWeight.NORMAL, text.getFontSize()));
            graphics.setFill(material.getFill());
            graphics.setStroke(material.getStroke());
            graphics.setLineWidth(material.getStrokeWidth());

            graphics.fillText(text.getContent(), 0, 0);
            if(material.getStroke() != null && material.getStrokeWidth() != 0) {
                graphics.strokeText(text.getContent(), 0, 0);
            }

            graphics.restore();
        }
    }
}
