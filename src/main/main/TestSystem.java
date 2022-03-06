import alphine.components.MaterialComponent;
import alphine.components.TextComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.System;
import javafx.scene.paint.Color;

public class TestSystem extends System {

    @Override
    public void onStart() {
        Entity text = new Entity("Hi");

        text.addComponent(new TextComponent().setContent("Hello World!").setFont("Times New Roman").setFontSize(12));
        text.addComponent(new TransformComponent().setPosition(100, 100));
        text.addComponent(new MaterialComponent().setFill(Color.RED).setStroke(Color.BLUE).setStrokeWidth(2));

        addEntity(text);
    }
}
