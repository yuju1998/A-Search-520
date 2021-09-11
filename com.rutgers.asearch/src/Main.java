import javafx.util.Pair;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

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

        public double aStarSearch(Tuple<Integer, Integer> start, Tuple<Integer, Integer> end, Grid grid, BiFunction<Tuple<Integer, Integer>, Tuple<Integer, Integer>, Double> heuristic) {
            if (start == end || grid.getCell(start) == null || grid.getCell(end) == null)
                return 0;
            PriorityQueue<GridCell> priorityQueue = new PriorityQueue<>(new GridCellComparator());
            grid.getCell(start).setCost(0);
            priorityQueue.add(grid.getCell(start));
            GridCell currentCell;
            double previousCost = 0;
            HashSet<GridCell> visited = new HashSet<>();
            while (!priorityQueue.isEmpty()) {
                currentCell = priorityQueue.poll();
                previousCost = currentCell.getCost();
                if (currentCell.equals(grid.getCell(end))) {
                    return previousCost;
                }
                Tuple<Integer, Integer> right = new Tuple<>(currentCell.getX() + 1, currentCell.getY());
                Tuple<Integer, Integer> left = new Tuple<>(currentCell.getX() - 1, currentCell.getY());
                Tuple<Integer, Integer> up = new Tuple<>(currentCell.getX(), currentCell.getY() - 1);
                Tuple<Integer, Integer> down = new Tuple<>(currentCell.getX(), currentCell.getY() + 1);
                GridCell rightChild = grid.getCell(right);
                GridCell leftChild = grid.getCell(left);
                GridCell upperChild = grid.getCell(up);
                GridCell downChild = grid.getCell(down);

                if (rightChild != null && !rightChild.isBlocked() && !visited.contains(rightChild)) {
                    if (priorityQueue.contains(rightChild)) {
                        priorityQueue.remove(rightChild);
                    }
                    rightChild.setHeuristicCost(previousCost  + heuristic.apply(rightChild.getLocation(), end) + 1);
                    rightChild.setCost(previousCost + 1);
                    priorityQueue.add(rightChild);
                    visited.add(rightChild);
                }
                if (leftChild != null && !leftChild.isBlocked() && !visited.contains(leftChild)) {
                    if (priorityQueue.contains(leftChild)) {
                        priorityQueue.remove(leftChild);
                    }
                    leftChild.setHeuristicCost(previousCost + heuristic.apply(leftChild.getLocation(), end) + 1);
                    leftChild.setCost(previousCost + 1);
                    priorityQueue.add(leftChild);
                    visited.add(leftChild);
                }
                if (upperChild != null && !upperChild.isBlocked() && !visited.contains(upperChild)) {
                    if (priorityQueue.contains(upperChild)) {
                        priorityQueue.remove(upperChild);
                    }
                    upperChild.setHeuristicCost(previousCost  + heuristic.apply(upperChild.getLocation(), end) + 1);
                    upperChild.setCost(previousCost + 1);
                    priorityQueue.add(upperChild);
                    visited.add(upperChild);
                }
                if (downChild != null && !downChild.isBlocked() && !visited.contains(downChild)) {
                    if (priorityQueue.contains(downChild)) {
                        priorityQueue.remove(downChild);
                    }
                    downChild.setHeuristicCost(previousCost + heuristic.apply(downChild.getLocation(), end) + 1);
                    downChild.setCost(previousCost + 1);
                    priorityQueue.add(downChild);
                    visited.add(downChild);
                }

            }


            return -1;
        }
        class GridCellComparator implements Comparator<GridCell> {

            @Override
            public int compare(GridCell o1, GridCell o2) {
                return Double.compare(o1.getHeuristicCost(),o2.getHeuristicCost());
            }
        }
    }


    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int prob = Integer.parseInt(args[2]);
        Grid grid = new Grid(x,y,prob);
        System.out.println(grid);
        aStarSearchObject aStarSearchObject = new aStarSearchObject();
        System.out.println(aStarSearchObject.aStarSearch(new Tuple<>(0,0), new Tuple<>(x-1,y-1), grid, aStarSearchObject::euclideanDistance));
        System.out.println(aStarSearchObject.aStarSearch(new Tuple<>(0,0), new Tuple<>(x-1,y-1), grid, aStarSearchObject::manhattanDistance));
        System.out.println(aStarSearchObject.aStarSearch(new Tuple<>(0,0), new Tuple<>(x-1,y-1), grid, aStarSearchObject::chebyshevDistance));
    }



}
