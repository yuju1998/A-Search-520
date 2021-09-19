import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class Robot {
    private Tuple<Integer, Integer> current;
    private Tuple<Integer, Integer> goal;
    private boolean canSeeSideways;
    private HashSet<GridCell> blocked;

    public Robot(Tuple<Integer, Integer> start, Tuple<Integer, Integer> goal, boolean canSeeSideways) {
        this.current = start;
        this.goal = goal;
        this.canSeeSideways = canSeeSideways;
        this.blocked = new HashSet<>();
    }

    public Tuple<Integer, Integer> getLocation() {
        return current;
    }

    public Tuple<Integer, Integer> getGoal() {
        return goal;
    }

    public void move(Tuple<Integer, Integer> pos) {
        current = pos;
    }

    public HashSet<GridCell> getKnownObstacles() {
        return blocked;
    }

    public void addObstacle(GridCell obstacle) {
        blocked.add(obstacle);
    }

    // attempt to follow a path, updating known obstacles along the way
    // stops prematurely if it bumps into an obstacle
    // returns the number of steps successfully traveled
    public int run(List<Tuple<Integer, Integer>> path, Grid grid) {
        int numStepsTaken = 0;
        for(Tuple<Integer, Integer> position : path) {
            if(this.canSeeSideways) { // update obstacles based on fov
                ArrayList<Tuple<Integer, Integer>> directions = new ArrayList<>(4);
                directions.add(new Tuple<>(this.current.f1 + 1, this.current.f2)); // right
                directions.add(new Tuple<>(this.current.f1 - 1, this.current.f2)); // left
                directions.add(new Tuple<>(this.current.f1, this.current.f2 - 1)); // up
                directions.add(new Tuple<>(this.current.f1, this.current.f2 + 1)); // down

                for(Tuple<Integer, Integer> direction : directions) {
                    GridCell cell = grid.getCell(direction);
                    if(cell != null && cell.isBlocked()) this.blocked.add(cell);
                }
            }

            if(grid.getCell(position).isBlocked()) { // if bump into an obstacle, stop
                this.blocked.add(grid.getCell(position));
                break;
            } else {
                numStepsTaken++;
                this.move(position);
            }
        }

        return numStepsTaken;
    }
}
