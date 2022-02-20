package ecs.scripts;

import ecs.Entity;

public abstract class Script {
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void onStart() { }

    public void onUpdate(double deltaTime) { }

    public void onRender() { }

    public void onEnd() { }
}

