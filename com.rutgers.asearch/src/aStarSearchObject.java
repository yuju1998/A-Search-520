import java.util.*;
import java.util.function.BiFunction;

public class aStarSearchObject {

    private BiFunction<Tuple<Integer, Integer>, Tuple<Integer, Integer>, Double> heuristic;

    public aStarSearchObject(BiFunction<Tuple<Integer, Integer>, Tuple<Integer, Integer>, Double> heuristic) {
        this.heuristic = heuristic;
    }

    public static double euclideanDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
        double tempX = Math.pow(currentCoordinate.f1 - goalCoordinate.f1, 2);
        double tempY = Math.pow(currentCoordinate.f2 - goalCoordinate.f2, 2);

        return Math.sqrt(tempX + tempY);
    }

    public static double manhattanDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
        double tempX = Math.abs(currentCoordinate.f1 - goalCoordinate.f1);
        double tempY = Math.abs(currentCoordinate.f2 - goalCoordinate.f2);

        return tempX + tempY;
    }

    public static double chebyshevDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
        double tempX = Math.abs(currentCoordinate.f1 - goalCoordinate.f1);
        double tempY = Math.abs(currentCoordinate.f2 - goalCoordinate.f2);

        return Math.max(tempX, tempY);
    }

    public GridWorldInfo aStarSearch(Tuple<Integer, Integer> start, Tuple<Integer, Integer> end, Grid grid, HashSet<GridCell> blocked) {
        if (start == end || grid.getCell(start) == null || grid.getCell(end) == null) return null;

        // create fringe and process start cell
        PriorityQueue<GridCell> fringe = new PriorityQueue<>(new GridCellComparator());
        grid.getCell(start).setCost(0);
        grid.getCell(start).setPrev(null);
        fringe.add(grid.getCell(start));

        // begin processing cells
        GridCell currentCell;
        double previousCost;
        HashSet<GridCell> discoveredCells = new HashSet<>();
        discoveredCells.add(grid.getCell(start));
        // HashSet<GridCell> visited = new HashSet<>(); -> not needed for consistent heuristics
        int numberOfCellsProcessed = 0;
        while (!fringe.isEmpty()) {

            numberOfCellsProcessed++;
            currentCell = fringe.poll();
            if(!currentCell.isBlocked()) {
                previousCost = currentCell.getCost();
                if (currentCell.equals(grid.getCell(end))) {
                    // goal found, reconstruct path
                    LinkedList<Tuple<Integer, Integer>> path = new LinkedList<>();
                    while(currentCell != null) {
                        path.push(currentCell.getLocation());
                        currentCell = currentCell.getPrev();
                    }
                    return new GridWorldInfo(previousCost, numberOfCellsProcessed, path);
                }

                // else, generate the children of currentCell
                ArrayList<Tuple<Integer, Integer>> directions = new ArrayList<>(4);
                directions.add(new Tuple<>(currentCell.getX() + 1, currentCell.getY())); // right
                directions.add(new Tuple<>(currentCell.getX() - 1, currentCell.getY())); // left
                directions.add(new Tuple<>(currentCell.getX(), currentCell.getY() - 1)); // up
                directions.add(new Tuple<>(currentCell.getX(), currentCell.getY() + 1)); // down

                // process each child
                for(Tuple<Integer, Integer> direction : directions) {
                    GridCell child = grid.getCell(direction);
                    if(child == null || blocked.contains(child)) continue; // check that cell is valid
                    if(!discoveredCells.contains(child)) { // first time processing this cell -> initialize and insert into fringe
                        child.setHeuristicCost(heuristic.apply(child.getLocation(), end));
                        child.setCost(previousCost + 1);
                        child.setPrev(currentCell);
                        fringe.add(child);
                        discoveredCells.add(child);
                    } else if(previousCost + 1 < child.getCost()) { // already in queue -> check if priority needs to be updated
                        fringe.remove(child);
                        child.setCost(previousCost + 1);
                        child.setPrev(currentCell);
                        fringe.add(child);
                    }
                }

                // GridCell rightChild = grid.getCell(right);
                // if (rightChild != null && !visited.contains(rightChild)) {
                //     if (priorityQueue.contains(rightChild)) {
                //         priorityQueue.remove(rightChild);
                //     }
                //     rightChild.setHeuristicCost(previousCost + heuristic.apply(rightChild.getLocation(), end) + 1);
                //     rightChild.setCost(previousCost + 1);
                //     visited.add(rightChild);
                //     if(!rightChild.isBlocked() || allowBumps){
                //         priorityQueue.add(rightChild);
                //     }
                //
                // }
            }
        }

        // path not found
        return new GridWorldInfo(Double.NaN, numberOfCellsProcessed, null);
    }

    class GridCellComparator implements Comparator<GridCell> {

        @Override
        public int compare(GridCell o1, GridCell o2) {
            return Double.compare(o1.getHeuristicCost(),o2.getHeuristicCost());
        }
    }
}
