// inspiration: https://www.youtube.com/watch?v=N1zDOjRJ8tQ

package ecs;

import ecs.components.Component;
import ecs.scripts.Script;
import ecs.worlds.World;

import java.util.UUID;

public class Entity {

    private String name;
    private final World world;
    private final UUID uuid;

    public Entity(String name, World world) {
        this.name = name;
        this.world = world;
        this.uuid = UUID.randomUUID();
    }

    public <T extends Component> T addComponent(T component) {
        return this.world.addComponent(this.uuid, component);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return this.world.getComponent(this.uuid, componentClass);
    }

    public <T extends Component> boolean hasComponent(Class<T> componentClass) {
        return this.world.hasComponent(this.uuid, componentClass);
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        this.world.removeComponent(this.uuid, componentClass);
    }

    public <T extends Script> T addScript(T script) {
        return this.world.addScript(this, script);
    }

    public <T extends Script> T getScript(Class<T> scriptClass) {
        return this.world.getScript(this.uuid, scriptClass);
    }

    public <T extends Script> boolean hasScript(Class<T> scriptClass) {
        return this.world.hasScript(this.uuid, scriptClass);
    }

    public <T extends Script> void removeScript(Class<T> scriptClass) {
        this.world.removeScript(this.uuid, scriptClass);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

