package ecs.worlds;

import ecs.Entity;
import ecs.components.Component;
import ecs.components.ComponentCollection;
import ecs.scripts.Script;
import ecs.scripts.ScriptCollection;
import ecs.systems.System;
import logging.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class World {
    private final List<Entity> entities = new ArrayList<>();
    private final HashMap<Type, ComponentCollection> components = new HashMap<>();
    private final HashMap<Type, ScriptCollection> scripts = new HashMap<>();

    private final List<System> systems = new ArrayList<>();

    public Entity createEntity(String name) {
        Entity entity = new Entity(name, this);
        entities.add(entity);
        return entity;
    }

    public void addSystem(System system) {
        system.setWorld(this);
        systems.add(system);
    }

    public <T extends Component> T addComponent(UUID entityId, T component) {
        if (components.containsKey(component.getClass())) {
            return components.get(component.getClass()).store(entityId, component);
        }

        ComponentCollection collection = new ComponentCollection();
        components.put(component.getClass(), collection);
        return collection.store(entityId, component);
    }

    public <T extends Component> T getComponent(UUID entityId, Class<T> component) {
        if (components.containsKey(component)) {
            return components.get(component).fetch(entityId);
        }

        throw new RuntimeException("Component not found");
    }

    public <T extends Component> boolean hasComponent(UUID entityId, Class<T> component) {
        return components.containsKey(component) && components.get(component).has(entityId);
    }

    public <T extends Component> void removeComponent(UUID entityId, Class<T> component) {
        if (components.containsKey(component)) {
            components.get(component).remove(entityId);
            return;
        }

        throw new RuntimeException("Component not found");
    }

    public <T extends Script> T addScript(Entity entity, T script) {
        script.setEntity(entity);

        if (scripts.containsKey(script.getClass())) {
            return scripts.get(script.getClass()).store(entity.getUuid(), script);
        }

        ScriptCollection collection = new ScriptCollection();
        scripts.put(script.getClass(), collection);
        return collection.store(entity.getUuid(), script);
    }

    public <T extends Script> T getScript(UUID entityId, Class<T> script) {
        if (scripts.containsKey(script)) {
            return scripts.get(script).fetch(entityId);
        }

        throw new RuntimeException("Script not found");
    }

    public <T extends Script> boolean hasScript(UUID entityId, Class<T> script) {
        return scripts.containsKey(script) && scripts.get(script).has(entityId);
    }

    public <T extends Script> void removeScript(UUID entityId, Class<T> script) {
        if (scripts.containsKey(script)) {
            scripts.get(script).remove(entityId);
            return;
        }

        throw new RuntimeException("Script not found");
    }

    public void start() {
        for (System system : systems) {
            try {
                system.onStart();
            } catch (Exception e) {
                Logger.error("Unhandled exception while starting system " + system.getClass().getSimpleName());
            }
        }

        for (ScriptCollection collection : scripts.values()) {
            for (Script script : collection.getScripts()) {
                try {
                    script.onStart();
                } catch (Exception e) {
                    Logger.error("Unhandled exception while starting script " + script.getClass().getSimpleName());
                }
            }
        }
    }

    public void update(double delta) {
        for (System system : systems) {
            try {
                system.onUpdate(delta);
            } catch (Exception e) {
                Logger.error(e, "Unhandled exception while updating system " + system.getClass().getSimpleName());
            }
        }

        for (ScriptCollection collection : scripts.values()) {
            for (Script script : collection.getScripts()) {
                try {
                    script.onUpdate(delta);
                } catch (Exception e) {
                    Logger.error(e, "Unhandled exception while updating script " + script.getClass().getSimpleName());
                }
            }
        }
    }

    public void render() {
        for (System system : systems) {
            try {
                system.onRender();
            } catch (Exception e) {
                Logger.error(e, "Unhandled exception while rendering system " + system.getClass().getSimpleName());
            }
        }

        for (ScriptCollection collection : scripts.values()) {
            for (Script script : collection.getScripts()) {
                try {
                    script.onRender();
                } catch (Exception e) {
                    Logger.error(e, "Unhandled exception while rendering script " + script.getClass().getSimpleName());
                }
            }
        }
    }

    public void end() {
        for (System system : systems) {
            try {
                system.onEnd();
            } catch (Exception e) {
                Logger.error(e, "Unhandled exception while ending system " + system.getClass().getSimpleName());
            }
        }

        for (ScriptCollection collection : scripts.values()) {
            for (Script script : collection.getScripts()) {
                try {
                    script.onEnd();
                } catch (Exception e) {
                    Logger.error(e, "Unhandled exception while ending script " + script.getClass().getSimpleName());
                }
            }
        }
    }

    public <T extends Component> Entity[] getEntities(Class<T>[] componentFilter) {
        List<Entity> filtered = new ArrayList<>();

        for (Entity entity : entities) {
            boolean valid = true;

            for (Class<T> component : componentFilter) {
                if (!entity.hasComponent(component)) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                filtered.add(entity);
            }
        }

        return filtered.toArray(new Entity[0]);
    }
}
