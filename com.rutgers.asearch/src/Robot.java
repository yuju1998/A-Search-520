import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Robot {
    private Tuple<Integer, Integer> current;
    private Tuple<Integer, Integer> goal;
    private Tuple<Integer, Integer> restartPoint;
    private boolean canSeeSideways;
    private HashSet<GridCell> blocked;
    private HashSet<GridCell> free;
    private Grid grid;
    private SearchAlgo searchAlgo;

    public Robot(Tuple<Integer, Integer> start, Tuple<Integer, Integer> goal, boolean canSeeSideways, Grid grid, SearchAlgo searchAlgo) {
        this.current = start;
        this.restartPoint = this.current;
        this.goal = goal;
        this.canSeeSideways = canSeeSideways;
        this.blocked = new HashSet<>();
        this.free = new HashSet<>();
        this.free.add(grid.getCell(start)); // start cell is always known/assumed to be free
        this.grid = grid;
        this.searchAlgo = searchAlgo;
    }

    public Tuple<Integer, Integer> getLocation() {
//        System.out.println("Curr:" + current);
//        System.out.println("Restart:" + restartPoint);
        if(current.equals(goal))
            return goal;
        return current.equals(restartPoint) ? current : restartPoint;

    }

    public Tuple<Integer, Integer> getRestartPoint(){
        return restartPoint;
    }

    public void setRestartPoint(Tuple<Integer, Integer> pos){
        restartPoint = pos;
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

    public HashSet<GridCell> getKnownFreeSpaces() {
        return free;
    }

    public void addObstacle(GridCell obstacle) {
        blocked.add(obstacle);
    }

    public void addFreeSpace(GridCell freeSpace) {
        free.add(freeSpace);
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
    private int runPath(List<Tuple<Integer, Integer>> path, int backtrackDistance) {
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
                    if(cell != null) {
                        if(cell.isBlocked()) addObstacle(cell);
                        else addFreeSpace(cell);
                    }
                }
            }

            GridCell nextCell = grid.getCell(position);
            if(nextCell.isBlocked()) { // if bump into an obstacle, stop
                addObstacle(nextCell);
                break;
            } else {
                numStepsTaken++;
                addFreeSpace(nextCell);
                move(position);
                if(numStepsTaken % backtrackDistance == 0){
                    setRestartPoint(position);
                }
            }
        }
        return numStepsTaken;
    }

    public GridWorldInfo run() {
       return run(1);
    }

    public GridWorldInfo run(int backtrackDistance) {
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
            int stepsTaken = runPath(result.getPath(), backtrackDistance );
            gridWorldInfoGlobal.addTrajectoryLength(stepsTaken);
            gridWorldInfoGlobal.addCellsProcessed(result.getNumberOfCellsProcessed());
            gridWorldInfoGlobal.getPath().addAll(result.getPath().subList(0, stepsTaken));
        }

        return gridWorldInfoGlobal;
    }
}
