import java.util.*;

// just tests the search algos to make sure they work
// usage: java Test xSize ySize blockedProbability%
public class Test {
    public static void printResults(GridWorldInfo result, Grid world, Robot rob) {
        System.out.println("num cells expanded: " + result.getNumberOfCellsProcessed());
        System.out.println("trajectory length: " + result.getTrajectoryLength());

        HashSet<GridCell> trajectory = new HashSet<>();
        trajectory.add(world.getCell(new Tuple<Integer, Integer>(0, 0)));
        if(result.getPath() == null) {
            System.out.println("Path not found.");
        } else {
            for(Tuple<Integer, Integer> coord : result.getPath()) {
                trajectory.add(world.getCell(coord));
            }
        }
        for(int j = 0; j < world.getYSize(); ++j) {
            for(int i = 0; i < world.getXSize(); ++i) {
                GridCell cell = world.getCell(new Tuple<>(i, j));
                String symbol = "o"; // default symbol
                if(trajectory.contains(cell)) symbol = "\u001B[36m-\u001B[0m";
                else if(rob.getKnownObstacles().contains(cell)) symbol = "\u001B[33mx\u001B[0m";
                else if(cell.isBlocked()) symbol = "\u001B[31mx\u001B[0m";
                System.out.print(symbol);
            }
            System.out.print('\n');
        }
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int prob = Integer.parseInt(args[2]);
        Grid world = new Grid(x, y, prob);

        // test repeated A* search
        System.out.println("Testing Repeated A* ...");
        aStarSearchObject aso = new aStarSearchObject(Heuristics::euclideanDistance);
        Tuple<Integer, Integer> start = new Tuple<>(0, 0);
        Tuple<Integer, Integer> end = new Tuple<>(x-1, y-1);
        Robot rob = new Robot(start, end, true);
        GridWorldInfo result = Main.runRobot(rob, world, aso::aStarSearch);
        printResults(result, world, rob);
        System.out.println();

        // test regular A* search
        System.out.println("Testing A* ...");
        result = aso.aStarSearch(start, end, world, cell -> cell.isBlocked());
        printResults(result, world, new Robot(start, end, true));
    }
}
