import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Robot {
    private Tuple<Integer, Integer> current;
    private Tuple<Integer, Integer> goal;
    private boolean canSeeSideways;
    private HashSet<GridCell> blocked;
    private Grid grid;
    private SearchAlgo searchAlgo;

    public Robot(Tuple<Integer, Integer> start, Tuple<Integer, Integer> goal, boolean canSeeSideways, Grid grid, SearchAlgo searchAlgo) {
        this.current = start;
        this.goal = goal;
        this.canSeeSideways = canSeeSideways;
        this.blocked = new HashSet<>();
        this.grid = grid;
        this.searchAlgo = searchAlgo;
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

    public Grid getGrid() {
        return grid;
    }

    public SearchAlgo getSearchAlgo() {
        return searchAlgo;
    }

    // attempt to follow a path, updating known obstacles along the way
    // stops prematurely if it bumps into an obstacle
    // returns the number of steps succesfully moved
    private int runPath(List<Tuple<Integer, Integer>> path) {
        int numStepsTaken = 0;
        for(Tuple<Integer, Integer> position : path) {
            if(canSeeSideways) { // update obstacles based on fov
                ArrayList<Tuple<Integer, Integer>> directions = new ArrayList<>(4);
                directions.add(new Tuple<>(current.f1 + 1, current.f2)); // right
                directions.add(new Tuple<>(current.f1 - 1, current.f2)); // left
                directions.add(new Tuple<>(current.f1, current.f2 - 1)); // up
                directions.add(new Tuple<>(current.f1, current.f2 + 1)); // down

                for(Tuple<Integer, Integer> direction : directions) {
                    GridCell cell = grid.getCell(direction);
                    if(cell != null && cell.isBlocked()) addObstacle(cell);
                }
            }
            if(grid.getCell(position).isBlocked()) { // if bump into an obstacle, stop
                addObstacle(grid.getCell(position));
                break;
            } else {
                numStepsTaken++;
                move(position);
            }
        }
        return numStepsTaken;
    }

    public GridWorldInfo run() {
        GridWorldInfo gridWorldInfoGlobal = new GridWorldInfo(0, 0, new ArrayList<>());
        // loop while robot has not reached the destination
        while(!getLocation().f1.equals(getGoal().f1) || !getLocation().f2.equals(getGoal().f2)) {
            // find path
            GridWorldInfo result = getSearchAlgo().search(getLocation(), getGoal(), getGrid(), getKnownObstacles()::contains);

            // if no path found, exit with failure
            if(result == null || result.getPath() == null) {
                gridWorldInfoGlobal.setPath(null);
                gridWorldInfoGlobal.setTrajectoryLength(Double.NaN);
                return gridWorldInfoGlobal;
            }

            // attempt to travel down returned path, and update statistics
            int stepsTaken = runPath(result.getPath());
            gridWorldInfoGlobal.addTrajectoryLength(stepsTaken);
            gridWorldInfoGlobal.addCellsProcessed(result.getNumberOfCellsProcessed());
            gridWorldInfoGlobal.getPath().addAll(result.getPath().subList(0, stepsTaken));
        }

        return gridWorldInfoGlobal;
    }
}
