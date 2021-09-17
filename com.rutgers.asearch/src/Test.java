import java.util.*;

public class Test {
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int prob = Integer.parseInt(args[2]);
        Grid world = new Grid(x, y, prob);
        aStarSearchObject aso = new aStarSearchObject(aStarSearchObject::manhattanDistance);
        GridWorldInfo result = aso.aStarSearch(
            new Tuple<Integer, Integer>(0, 0),
            new Tuple<Integer, Integer>(x-1, y-1),
            world,
            cell -> cell.isBlocked()
        );

        // print results
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
        for(int j = 0; j < y; ++j) {
            for(int i = 0; i < x; ++i) {
                GridCell cell = world.getCell(new Tuple<>(i, j));
                if(trajectory.contains(cell)) System.out.print("\u001B[36m-\u001B[0m");
                else System.out.print(cell.isBlocked() ? "\u001B[31mx\u001B[0m" : "o");
            }
            System.out.print('\n');
        }
    }
}
