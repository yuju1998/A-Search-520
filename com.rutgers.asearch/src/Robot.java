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
    private int runPath(List<Tuple<Integer, Integer>> path, Grid grid) {
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
                return -1;
            } else if(grid.getCell(position).equals(goal)){
                return 1;
            }else {
                this.move(position);
            }
        }
        return 1;
    }

    public GridWorldInfo run() {
        GridWorldInfo gridWorldInfoGlobal = new GridWorldInfo(0,0,null);
        boolean shouldContinue = true;
        while(shouldContinue) {
            // find path
            GridWorldInfo result = getSearchAlgo().search(getLocation(), getGoal(), getGrid(), getKnownObstacles()::contains);
            if(result == null || result.getPath() == null) {
                gridWorldInfoGlobal.getPath().clear();
                gridWorldInfoGlobal.setTrajectoryLength(0);
                return gridWorldInfoGlobal;
            }
            gridWorldInfoGlobal.addTrajectoryLength(result.getTrajectoryLength());
            gridWorldInfoGlobal.addCellsProcessed(result.getNumberOfCellsProcessed());
            gridWorldInfoGlobal.setPath(result.getPath());
            shouldContinue = runPath(result.getPath(), getGrid()) == -1;
        }

        return gridWorldInfoGlobal;
    }
}
