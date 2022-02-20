package ecs.systems;

import ecs.Entity;
import ecs.components.Component;
import ecs.worlds.World;

public abstract class System {

    public World world;

    public void setWorld(World world) {
        this.world = world;
    }

    public Entity createEntity(String name) {
        return world.createEntity(name);
    }

    public <T extends Component> Entity[] getEntities(Class<T> filter) {
        return world.getEntities(new Class[] { filter });
    }

    public <T1 extends Component, T2 extends Component> Entity[] getEntities(Class<T1> filter1, Class<T2> filter2) {
        return world.getEntities(new Class[] { filter1, filter2 });
    }

    public <T1 extends Component, T2 extends Component, T3 extends Component> Entity[] getEntities(Class<T1> filter1, Class<T2> filter2, Class<T3> filter3) {
        return world.getEntities(new Class[] { filter1, filter2, filter3 });
    }

    public void onStart() { }

    public void onUpdate(double deltaTime) { }

    public void onRender() { }

    public void onEnd() { }
}

