package ecs.scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScriptCollection {
    private final List<Script> scripts = new ArrayList<>();
    private final HashMap<UUID, Integer> mappings = new HashMap<>();

    public <T extends Script> T store(UUID entityId, T script) {
        scripts.add(script);
        mappings.put(entityId, scripts.size() - 1);

        return script;
    }

    public <T extends Script> T fetch(UUID entityId) {
        int index = mappings.get(entityId);
        return ((T) scripts.get(index));
    }

    public void remove(UUID entityId) {
        int index = mappings.get(entityId);
        scripts.remove(index);
        mappings.remove(entityId);
    }

    public boolean has(UUID entityId) {
        return mappings.containsKey(entityId);
    }

    public List<Script> getScripts() {
        return scripts;
    }
}
