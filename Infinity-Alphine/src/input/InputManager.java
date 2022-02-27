package input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.dyn4j.geometry.Vector2;

import java.util.HashMap;
import java.util.Map;

public class InputManager {

    private static InputManager instance = new InputManager();

    public static InputManager getInstance() {
        return instance;
    }

    private final Map<KeyCode, Boolean> keys = new HashMap<>();
    private final Map<MouseButton, Boolean> mouse = new HashMap<>();

    private Vector2 mousePosition = new Vector2(0, 0);
    private boolean mouseOnScreen = true;
    private Vector2 deltaScroll = new Vector2(0, 0);

    public void setKeyPressed(KeyCode key, boolean state) {
        keys.put(key, state);
    }

    public void setMousePosition(Vector2 position) {
        mousePosition = position;
    }

    public void setMouseEntered(boolean state) {
        mouseOnScreen = state;
    }

    public void setMouseScroll(Vector2 delta) {
        deltaScroll = delta;
    }

    public void setMousePressed(MouseButton button, boolean state) {
        mouse.put(button, state);
    }

    public boolean isKeyPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public boolean isMouseInWindow() {
        return mouseOnScreen;
    }

    public Vector2 getMouseScroll() {
        return deltaScroll;
    }

    public boolean isMousePressed(MouseButton button) {
        return mouse.getOrDefault(button, false);
    }
}
