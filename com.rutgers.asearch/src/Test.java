//import java.util.*;
//
//// just tests the search algos to make sure they work
//// usage: java Test xSize ySize blockedProbability%
//public class Test {
//    public static void printResults(GridWorldInfo result, Grid world) {
//        System.out.println("num cells expanded: " + result.getNumberOfCellsProcessed());
//        System.out.println("trajectory length: " + result.getTrajectoryLength());
//
//        HashSet<GridCell> trajectory = new HashSet<>();
//        trajectory.add(world.getCell(new Tuple<Integer, Integer>(0, 0)));
//        if(result.getPath() == null) {
//            System.out.println("Path not found.");
//        } else {
//            for(Tuple<Integer, Integer> coord : result.getPath()) {
//                trajectory.add(world.getCell(coord));
//            }
//        }
//        for(int j = 0; j < world.getYSize(); ++j) {
//            for(int i = 0; i < world.getXSize(); ++i) {
//                GridCell cell = world.getCell(new Tuple<>(i, j));
//                if(trajectory.contains(cell)) System.out.print("\u001B[36m-\u001B[0m");
//                else System.out.print(cell.isBlocked() ? "\u001B[31mx\u001B[0m" : "o");
//            }
//            System.out.print('\n');
//        }
//    }
//
//    public static void main(String[] args) {
//        int x = Integer.parseInt(args[0]);
//        int y = Integer.parseInt(args[1]);
//        int prob = Integer.parseInt(args[2]);
//        Grid world = new Grid(x, y, prob);
//
//        // test A* search
//        System.out.println("Testing A-Star:");
//        aStarSearchObject aso = new aStarSearchObject(aStarSearchObject::manhattanDistance);
//        GridWorldInfo result = aso.aStarSearch(
//            new Tuple<Integer, Integer>(0, 0),
//            new Tuple<Integer, Integer>(x-1, y-1),
//            world,
//            cell -> cell.isBlocked()
//        );
//        printResults(result, world);
//
//        // test BFS
//        System.out.println("\nTesting BFS");
//        result = BFSObject.BFS(
//            new Tuple<Integer, Integer>(0, 0),
//            new Tuple<Integer, Integer>(x-1, y-1),
//            world,
//            cell -> cell.isBlocked()
//        );
//        printResults(result, world);
//    }
//}
