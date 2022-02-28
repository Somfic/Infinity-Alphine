package systems;

import ecs.System;
import ecs.World;
import input.InputManager;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import logging.Logger;
import org.dyn4j.geometry.Vector2;

public class InputSystem extends System {

    private Scene scene;

    private KeyEvent keyPressedEvent;
    private KeyEvent keyReleasedEvent;
    private MouseEvent mouseMovedEvent;
    private MouseEvent mouseEnteredEvent;
    private MouseEvent mouseExitedEvent;
    private ScrollEvent scrollEvent;
    private MouseEvent mousePressedEvent;
    private MouseEvent mouseReleasedEvent;

    public InputSystem(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void onStart(World world) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);

        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, this::onMouseMoved);

        scene.addEventHandler(MouseEvent.MOUSE_ENTERED, this::onMouseEntered);
        scene.addEventHandler(MouseEvent.MOUSE_EXITED, this::onMouseExited);

        scene.addEventHandler(ScrollEvent.SCROLL, this::onScroll);
    }

    @Override
    public void onUpdate(double dt) {
        if(keyPressedEvent != null) {
            // todo: add shift and other modifiers
            keyPressedEvent = null;
        }

        if(keyReleasedEvent != null) {
            InputManager.getInstance().setKeyPressed(keyReleasedEvent.getCode(), false);
            keyReleasedEvent = null;
        }

        if(mouseMovedEvent != null) {
            InputManager.getInstance().setMousePosition(new Vector2(mouseMovedEvent.getX(), mouseMovedEvent.getY()));
            mouseMovedEvent = null;
        }

        if(mouseEnteredEvent != null) {
            InputManager.getInstance().setMouseEntered(true);
            mouseEnteredEvent = null;
        }

        if(mouseExitedEvent != null) {
            InputManager.getInstance().setMouseEntered(false);
            mouseExitedEvent = null;
        }

        if(scrollEvent != null) {
            InputManager.getInstance().setMouseScroll(new Vector2(scrollEvent.getDeltaX(), scrollEvent.getDeltaY()));
            scrollEvent = null;
        }

        if(mousePressedEvent != null) {
            InputManager.getInstance().setMousePressed(mousePressedEvent.getButton(), true);
            mousePressedEvent = null;
        }

        if(mouseReleasedEvent != null) {
            InputManager.getInstance().setMousePressed(mouseReleasedEvent.getButton(), false);
            mouseReleasedEvent = null;
        }
    }

    private void onKeyPressed(KeyEvent e) {
        keyPressedEvent = e;
    }

    private void onKeyReleased(KeyEvent e) {
        keyReleasedEvent = e;
    }

    private void onMouseMoved(MouseEvent e) {
        mouseMovedEvent = e;
    }

    private void onMouseEntered(MouseEvent e) {
        mouseEnteredEvent = e;
    }

    private void onMouseExited(MouseEvent e) {
        mouseExitedEvent = e;
    }

    private void onScroll(ScrollEvent e) {
        scrollEvent = e;
    }

    private void onMousePressed(MouseEvent e) {
        mousePressedEvent = e;
    }

    private void onMouseReleased(MouseEvent e) {
        mouseReleasedEvent = e;
    }
}
