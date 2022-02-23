package components;

import ecs.Component;

public class Collider extends Component {
    private ColliderType type;

    public Collider(ColliderType type) {
        this.type = type;
    }

    public ColliderType getType() {
        return type;
    }

    public Collider setType(ColliderType type) {
        this.type = type;
        return this;
    }
}

