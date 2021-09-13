import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiFunction;

public class Main {

    static class aStarSearchObject {

        public double euclideanDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
            double tempX = Math.pow(currentCoordinate.f1 - goalCoordinate.f1, 2);
            double tempY = Math.pow(currentCoordinate.f2 - goalCoordinate.f2, 2);

            return Math.sqrt(tempX + tempY);
        }

        public double manhattanDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
            double tempX = Math.abs(currentCoordinate.f1 - goalCoordinate.f1);
            double tempY = Math.abs(currentCoordinate.f2 - goalCoordinate.f2);

            return tempX + tempY;
        }

        public double chebyshevDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
            double tempX = Math.abs(currentCoordinate.f1 - goalCoordinate.f1);
            double tempY = Math.abs(currentCoordinate.f2 - goalCoordinate.f2);

            return Math.max(tempX, tempY);
        }

        public GridWorldInfo aStarSearch(Tuple<Integer, Integer> start, Tuple<Integer, Integer> end, Grid grid, BiFunction<Tuple<Integer, Integer>, Tuple<Integer, Integer>, Double> heuristic, boolean allowBumps) {
            if (start == end || grid.getCell(start) == null || grid.getCell(end) == null)
                return null;
            PriorityQueue<GridCell> priorityQueue = new PriorityQueue<>(new GridCellComparator());
            grid.getCell(start).setCost(0);
            priorityQueue.add(grid.getCell(start));
            GridCell currentCell;
            double previousCost;
            HashSet<GridCell> visited = new HashSet<>();
            int numberOfCellsProcessed = 0;
            while (!priorityQueue.isEmpty()) {

                numberOfCellsProcessed++;
                currentCell = priorityQueue.poll();
                if(!currentCell.isBlocked()) {
                    previousCost = currentCell.getCost();
                    if (currentCell.equals(grid.getCell(end))) {
                        return new GridWorldInfo(previousCost, numberOfCellsProcessed);
                    }
                    Tuple<Integer, Integer> right = new Tuple<>(currentCell.getX() + 1, currentCell.getY());
                    Tuple<Integer, Integer> left = new Tuple<>(currentCell.getX() - 1, currentCell.getY());
                    Tuple<Integer, Integer> up = new Tuple<>(currentCell.getX(), currentCell.getY() - 1);
                    Tuple<Integer, Integer> down = new Tuple<>(currentCell.getX(), currentCell.getY() + 1);
                    GridCell rightChild = grid.getCell(right);
                    GridCell leftChild = grid.getCell(left);
                    GridCell upperChild = grid.getCell(up);
                    GridCell downChild = grid.getCell(down);

                    if (rightChild != null && !visited.contains(rightChild)) {
                        if (priorityQueue.contains(rightChild)) {
                            priorityQueue.remove(rightChild);
                        }
                        rightChild.setHeuristicCost(previousCost + heuristic.apply(rightChild.getLocation(), end) + 1);
                        rightChild.setCost(previousCost + 1);
                       visited.add(rightChild);
                        if(!rightChild.isBlocked() || allowBumps){
                            priorityQueue.add(rightChild);
                        }

                    }
                    if (leftChild != null && !visited.contains(leftChild)) {
                        if (priorityQueue.contains(leftChild)) {
                            priorityQueue.remove(leftChild);
                        }
                        leftChild.setHeuristicCost(previousCost + heuristic.apply(leftChild.getLocation(), end) + 1);
                        leftChild.setCost(previousCost + 1);
                        visited.add(leftChild);
                        if(!leftChild.isBlocked() || allowBumps){
                           priorityQueue.add(leftChild);
                        }
                    }
                    if (upperChild != null && !visited.contains(upperChild)) {
                        if (priorityQueue.contains(upperChild)) {
                            priorityQueue.remove(upperChild);
                        }
                        upperChild.setHeuristicCost(previousCost + heuristic.apply(upperChild.getLocation(), end) + 1);
                        upperChild.setCost(previousCost + 1);
                        visited.add(upperChild);
                        if(!upperChild.isBlocked() || allowBumps){
                            priorityQueue.add(upperChild);
                        }
                    }
                    if (downChild != null && !visited.contains(downChild)) {
                        if (priorityQueue.contains(downChild)) {
                            priorityQueue.remove(downChild);
                        }
                        downChild.setHeuristicCost(previousCost + heuristic.apply(downChild.getLocation(), end) + 1);
                        downChild.setCost(previousCost + 1);
                        visited.add(downChild);
                        if(!downChild.isBlocked() || allowBumps){
                            priorityQueue.add(downChild);
                        }
                    }
                }
            }

            return new GridWorldInfo(Double.NaN,numberOfCellsProcessed);
        }

        class GridCellComparator implements Comparator<GridCell> {

            @Override
            public int compare(GridCell o1, GridCell o2) {
                return Double.compare(o1.getHeuristicCost(),o2.getHeuristicCost());
            }
        }
    }

    public static void runProbabilitySimulation(int xDimension, int yDimension, int numberOfIterations, boolean allowBumps) {
        ArrayList<GridWorldInfo> solutionDensity = new ArrayList<>();
        for (int i = 0; i <= 33; i++){
            for (int j = 0; j<numberOfIterations; j++) {
                Grid grid = new Grid(xDimension, yDimension, i);
                aStarSearchObject aStarSearchObject = new aStarSearchObject();
                GridWorldInfo info = aStarSearchObject.aStarSearch(new Tuple<>(0, 0), new Tuple<>(xDimension - 1, yDimension - 1), grid, aStarSearchObject::euclideanDistance, allowBumps);
                info.setProbability(i);
                solutionDensity.add(info);
            }
        }
        String bumps = allowBumps ? "bumps" : "NoBumps";
        printResultsToCsv(xDimension + "x" + yDimension + bumps +"Result.csv", solutionDensity);

    }
    public static void printResultsToCsv(String fileName, List<GridWorldInfo> gridWorldInfo){
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {


            StringBuilder sb = new StringBuilder();
            sb.append("Probability");
            sb.append(',');
            sb.append("Solvable");
            sb.append(',');
            sb.append("TrajectoryLength");
            sb.append(',');
            sb.append("NumberOfCellsProcessed");
            sb.append('\n');
            writer.write(sb.toString());

            for (GridWorldInfo info: gridWorldInfo) {
                sb = new StringBuilder();
                sb.append(info.getProbability());
                sb.append(',');
                sb.append(!Double.isNaN(info.getTrajectoryLength()));
                sb.append(',');
                sb.append(info.getTrajectoryLength());
                sb.append(',');
                sb.append(info.getNumberOfCellsProcessed());
                sb.append('\n');
                writer.write(sb.toString());
            }


            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int iterations = args.length > 2 ? Integer.parseInt(args[2]): 1000;
        runProbabilitySimulation(x,y,iterations,true);
        runProbabilitySimulation(x,y,iterations,false);
    }





}
