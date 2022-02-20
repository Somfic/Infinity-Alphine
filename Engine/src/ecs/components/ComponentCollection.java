package ecs.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ComponentCollection {
    private final List<Component> components = new ArrayList<>();
    private final HashMap<UUID, Integer> mappings = new HashMap<>();

    public <T extends Component> T store(UUID entityId, T component) {
        components.add(component);
        mappings.put(entityId, components.size() - 1);

        return component;
    }

    public <T extends Component> T fetch(UUID entityId) {
        int index = mappings.get(entityId);
        return ((T) components.get(index));
    }

    public void remove(UUID entityId) {
        int index = mappings.get(entityId);
        components.remove(index);
        mappings.remove(entityId);
    }

    public boolean has(UUID entityId) {
        return mappings.containsKey(entityId);
    }
}
