import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Predicate;

public class Main {

    public static void runProbabilitySimulation(int xDimension, int yDimension, int numberOfIterations, boolean canSeeSideways) {
        ArrayList<GridWorldInfo> solutionDensity = new ArrayList<>();
        for (int i = 0; i <= 100; i++){
            for (int j = 0; j<numberOfIterations; j++) {
                Grid grid = new Grid(xDimension, yDimension, i);
                Robot robot = new Robot(new Tuple<> (0,0), new Tuple<>(xDimension-1,yDimension-1), canSeeSideways, grid , new AStarSearch((Heuristics::euclideanDistance)));
                GridWorldInfo info = robot.run();
                info.setProbability(i);
                solutionDensity.add(info);
            }
        }
        String bumps = canSeeSideways ? "SideFov" : "NoSideFov";
        printResultsToCsv(xDimension + "x" + yDimension + bumps +"Result.csv", solutionDensity);

    }

    public static void runQuestion6(int xDim, int yDim, int numIter, int maxProb) {
        String fileName = "Q6-AStar-Manhattan-NoBumps-Results.csv";
        List<GridWorldInfo> results = runThreeSearches(xDim, yDim, numIter, new AStarSearch(Heuristics::manhattanDistance), true, maxProb);
        printResultsToCsv(fileName, results);

        fileName = "Q6-AStar-Euclidean-NoBumps-Results.csv";
        results = runThreeSearches(xDim, yDim, numIter, new AStarSearch(Heuristics::euclideanDistance), true, maxProb);
        printResultsToCsv(fileName, results);

        fileName = "Q6-AStar-Chebyshev-NoBumps-Results.csv";
        results = runThreeSearches(xDim, yDim, numIter, new AStarSearch(Heuristics::chebyshevDistance), true, maxProb);
        printResultsToCsv(fileName, results);

        fileName = "Q6-BFS-NoBumps-Results.csv";
        results = runThreeSearches(xDim, yDim, numIter, BFSObject::BFS, true, maxProb);
        printResultsToCsv(fileName, results);
    }

    public static void runQuestion7(int xDim, int yDim, int numIter, int maxProb) {
        String fileName = "Q6-AStar-Manhattan-Bumps-Results.csv";
        List<GridWorldInfo> results = runThreeSearches(xDim, yDim, numIter, new AStarSearch(Heuristics::manhattanDistance), false, maxProb);
        printResultsToCsv(fileName, results);

        fileName = "Q6-AStar-Euclidean-Bumps-Results.csv";
        results = runThreeSearches(xDim, yDim, numIter, new AStarSearch(Heuristics::euclideanDistance), false, maxProb);
        printResultsToCsv(fileName, results);

        fileName = "Q6-AStar-Chebyshev-Bumps-Results.csv";
        results = runThreeSearches(xDim, yDim, numIter, new AStarSearch(Heuristics::chebyshevDistance), false, maxProb);
        printResultsToCsv(fileName, results);

        fileName = "Q6-BFS-Bumps-Results.csv";
        results = runThreeSearches(xDim, yDim, numIter, BFSObject::BFS, false, maxProb);
        printResultsToCsv(fileName, results);
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

    // args: x, y, number of iterations
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int iterations = args.length > 2 ? Integer.parseInt(args[2]): 1000;
        // runProbabilitySimulation(x,y,iterations,false);

        runQuestion6(x, y, iterations, 33);
        runQuestion7(x, y, iterations, 33);
    }

    private static List<GridWorldInfo> runThreeSearches(int xDim, int yDim, int numIter, SearchAlgo algo, boolean canSeeSideways, int maxProb) {
        // initialize variables needed for searching
        Tuple<Integer, Integer> start = new Tuple<>(0, 0);
        Tuple<Integer, Integer> end = new Tuple<>(xDim-1, yDim-1);

        ArrayList<GridWorldInfo> results = new ArrayList<>((maxProb+1)*numIter);
        for(int prob = 0; prob <= maxProb; ++prob) {
            for(int iter = 0; iter < numIter; ++iter) { // test each probability numIter times
                GridWorldInfo globalInfo = new GridWorldInfo(0, 0, new ArrayList<>()); // holds stats for this iteration
                Grid grid;
                while(true) { // keep generating gridworlds until one is solvable
                    grid = new Grid(xDim, yDim, prob);

                    // attempt search over complete gridworld and continue if it is solvable
                    GridWorldInfo completeResult = algo.search(start, end, grid, cell -> cell.isBlocked());
                    if(completeResult.getPath() != null) {
                        globalInfo.setTrajectoryLengthComplete(completeResult.getTrajectoryLength());
                        break;
                    }
                }

                // run Repeated Search and record results
                Robot robot = new Robot(start, end, canSeeSideways, grid, algo);
                GridWorldInfo repeatedResult = robot.run();
                globalInfo.setTrajectoryLength(repeatedResult.getTrajectoryLength());
                globalInfo.setNumberOfCellsProcessed(repeatedResult.getNumberOfCellsProcessed());
                globalInfo.setPath(repeatedResult.getPath());

                // run search over discovered gridworld and record results
                Predicate<GridCell> discoveredAndFree = robot.getKnownFreeSpaces()::contains;
                Predicate<GridCell> undiscoveredOrBlocked = discoveredAndFree.negate();
                GridWorldInfo discoveredResult = algo.search(start, end, grid, undiscoveredOrBlocked);
                globalInfo.setTrajectoryLengthDiscovered(discoveredResult.getTrajectoryLength());

                globalInfo.setProbability(prob);
                results.add(globalInfo);
            }
        }

        return results;
    }

}
