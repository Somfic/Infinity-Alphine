import alphine.components.MaterialComponent;
import alphine.components.TextComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.System;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FpsSystem extends System {

    Entity fpsComponent;

    @Override
    public void onStart() {
        fpsComponent = new Entity("FPS Counter");

        fpsComponent.addComponent(new TransformComponent().setPosition(10, 20));
        fpsComponent.addComponent(new TextComponent().setContent("FPS").setFont("Monospace").setFontSize(20));
        fpsComponent.addComponent(new MaterialComponent().setFill(Color.BLACK).setStrokeWidth(20));

        addEntity(fpsComponent);
    }

    final double smoothing = 0.9f;
    double averageFps = -1f;
    double averageDelta = -1f;

    @Override
    public void onUpdate(double dt) {
        double fps = 1f / dt;
        double deltaMillis = dt * 1e3;

        if (averageFps < 0) {
            averageFps = 60;
        } else {
            averageFps = averageFps * smoothing + fps * (1.0 - smoothing);
        }

        if (averageDelta < 0) {
            averageDelta = 16.666666;
        } else {
            averageDelta = averageDelta * smoothing + deltaMillis * (1.0 - smoothing);
        }

        TextComponent text = fpsComponent.getComponent(TextComponent.class);
        text.setContent(String.format("FPS: %.0f", averageFps));
    }
}
