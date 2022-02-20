package systems;

import ecs.systems.System;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logging.LogLevel;
import logging.LogListener;
import logging.Logger;

public class CanvasSystem extends System implements LogListener {

    private final Stage stage;
    private final Canvas canvas;
    private final boolean isDebug;
    private TextArea consoleArea;

    public CanvasSystem(Stage stage, Canvas canvas) {
        this.stage = stage;
        this.canvas = canvas;
        this.isDebug = false;
    }

    public CanvasSystem(Stage stage, Canvas canvas, boolean isDebug) {
        this.stage = stage;
        this.canvas = canvas;
        this.isDebug = isDebug;
    }

    @Override
    public void onStart() {
        if(!this.isDebug) {
            Logger.debug("Creating release mode canvas");

            // Resize
            this.stage.widthProperty().addListener((observable, oldValue, newValue) -> this.canvas.setWidth(newValue.doubleValue()));
            this.stage.heightProperty().addListener((observable, oldValue, newValue) -> this.canvas.setHeight(newValue.doubleValue()));

            Scene scene = new Scene(new Group(canvas));
            this.stage.setScene(scene);

        } else {
            Logger.debug("Creating debug mode canvas");

            // Main pane
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
        }

        this.stage.show();
    }

    @Override
    public void onLog(LogLevel level, Exception exception, String message) {
        consoleArea.appendText(message + "\n");
    }
}
