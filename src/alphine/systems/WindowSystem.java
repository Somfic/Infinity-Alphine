package alphine.systems;

import alphine.ecs.System;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import alphine.logging.LogLevel;
import alphine.logging.LogListener;
import alphine.logging.Logger;

public class WindowSystem extends System implements LogListener {

    private final Stage stage;
    private Scene scene;
    private final Canvas canvas;

    private TextArea consoleArea;

    public WindowSystem(Stage stage, Scene scene, Canvas canvas) {
        super();
        this.stage = stage;
        this.scene = scene;
        this.canvas = canvas;
    }

    @Override
    public void onStart() {
        if(!getWorld().isDebug()) {
            alphine.logging.Logger.debug("Creating release mode canvas");


            this.stage.setScene(scene);
            this.stage.show();

            double windowDeltaHeight = stage.getHeight() - scene.getHeight();
            double windowDeltaWidth = stage.getWidth() - scene.getWidth();;

            // Resize
            this.stage.widthProperty().addListener((observable, oldValue, newValue) -> this.canvas.setWidth(newValue.doubleValue() - windowDeltaWidth));
            this.stage.heightProperty().addListener((observable, oldValue, newValue) -> this.canvas.setHeight(newValue.doubleValue() - windowDeltaHeight));

        } else {
            alphine.logging.Logger.debug("Creating debug mode canvas");

            // game.Stars pane
            BorderPane mainPane = new BorderPane();

            // Canvas panel
            mainPane.setCenter(canvas);

            // Hierarchy panel
            TitledPane hierarchyPane = new TitledPane();
            hierarchyPane.setText("Hierarchy");
            mainPane.setLeft(hierarchyPane);

            // Hierarchy tree
            TreeView<String> hierarchyTree = new TreeView<>();
            hierarchyPane.setContent(hierarchyTree);

            // Properties panel
            TitledPane propertiesPane = new TitledPane();
            propertiesPane.setText("Properties");
            mainPane.setRight(propertiesPane);

            // Properties tree
            TreeView<String> propertiesTree = new TreeView<>();
            propertiesPane.setContent(propertiesTree);

            // Console panel
            TitledPane consolePane = new TitledPane();
            consolePane.setText("Console");
            mainPane.setBottom(consolePane);

            // Console messages
            consoleArea = new TextArea();
            consoleArea.setEditable(false);
            consolePane.setContent(consoleArea);

            // Scene
            Scene scene = new Scene(mainPane);
            this.stage.setScene(scene);

            Logger.addListener(this);

            this.stage.show();
        }
    }

    @Override
    public void onRender(GraphicsContext graphics) {
        graphics.setFill(Color.color(132f / 255f, 215f / 255f, 210f / 255f));
        graphics.fillRect(0, 0, (int)Math.ceil(canvas.getWidth()), (int)Math.ceil(canvas.getHeight()));
    }

    @Override
    public void onLog(LogLevel level, Exception exception, String message) {
        consoleArea.appendText(message + "\n");
    }
}