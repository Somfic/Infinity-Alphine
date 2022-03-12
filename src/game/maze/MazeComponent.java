package game.maze;

import alphine.ecs.Component;

public class MazeComponent extends Component {
    private boolean isPath;
    private boolean isWall;

    public MazeComponent setPath(boolean path) {
        isPath = path;
        isWall = !path;
        return this;
    }

    public boolean isPath() {
        return isPath;
    }

    public MazeComponent setWall(boolean wall) {
        isWall = wall;
        isPath = !wall;
        return this;
    }

    public boolean isWall() {
        return isWall;
    }
}
