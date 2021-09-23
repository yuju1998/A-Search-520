import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiFunction;
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
        runThreeSearches(fileName, xDim, yDim, numIter, new AStarSearch(Heuristics::manhattanDistance), true, maxProb);

        fileName = "Q6-AStar-Euclidean-NoBumps-Results.csv";
        runThreeSearches(fileName, xDim, yDim, numIter, new AStarSearch(Heuristics::euclideanDistance), true, maxProb);

        fileName = "Q6-AStar-Chebyshev-NoBumps-Results.csv";
        runThreeSearches(fileName, xDim, yDim, numIter, new AStarSearch(Heuristics::chebyshevDistance), true, maxProb);

        fileName = "Q6-BFS-NoBumps-Results.csv";
        runThreeSearches(fileName, xDim, yDim, numIter, BFSObject::BFS, true, maxProb);
    }

    public static void runQuestion7(int xDim, int yDim, int numIter, int maxProb) {
        String fileName = "Q7-AStar-Manhattan-Bumps-Results.csv";
        runThreeSearches(fileName, xDim, yDim, numIter, new AStarSearch(Heuristics::manhattanDistance), false, maxProb);

        fileName = "Q7-AStar-Euclidean-Bumps-Results.csv";
        runThreeSearches(fileName, xDim, yDim, numIter, new AStarSearch(Heuristics::euclideanDistance), false, maxProb);

        fileName = "Q7-AStar-Chebyshev-Bumps-Results.csv";
        runThreeSearches(fileName, xDim, yDim, numIter, new AStarSearch(Heuristics::chebyshevDistance), false, maxProb);

        fileName = "Q7-BFS-Bumps-Results.csv";
        runThreeSearches(fileName, xDim, yDim, numIter, BFSObject::BFS, false, maxProb);
    }

    public static void runQuestion9(int xDim, int yDim, int numIter, int prob, double maxWeight, double step) {
        String name1 = "Q9prob=" + prob + "-AStar-WeightedManhattan-NoBumps-Results.csv";
        String name2 = "Q9prob=" + prob + "-RepeatedAStar-WeightedManhattan-NoBumps-Results.csv";
        runWeightedSearch(name1, name2, xDim, yDim, numIter, Heuristics::manhattanDistance, prob, maxWeight, step);

        name1 = "Q9prob=" + prob + "-AStar-WeightedEuclidean-NoBumps-Results.csv";
        name2 = "Q9prob=" + prob + "-RepeatedAStar-WeightedEuclidean-NoBumps-Results.csv";
        runWeightedSearch(name1, name2, xDim, yDim, numIter, Heuristics::euclideanDistance, prob, maxWeight, step);

        name1 = "Q9prob=" + prob + "-AStar-WeightedChebyshev-NoBumps-Results.csv";
        name2 = "Q9prob=" + prob + "-RepeatedAStar-WeightedChebyshev-NoBumps-Results.csv";
        runWeightedSearch(name1, name2, xDim, yDim, numIter, Heuristics::chebyshevDistance, prob, maxWeight, step);

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

        // runQuestion6(x, y, iterations, 33);
        // runQuestion7(x, y, iterations, 33);
        runQuestion9(x, y, iterations, 15, 10.0, 0.2);
    }

    private static void runThreeSearches(String fileName, int xDim, int yDim, int numIter, SearchAlgo algo, boolean canSeeSideways, int maxProb) {
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

        printResultsToCsv(fileName, results);
    }

    private static void runWeightedSearch(
        String fileName1, String fileName2,
        int xDim, int yDim, int numIter,
        BiFunction<Tuple<Integer, Integer>, Tuple<Integer, Integer>, Double> heuristic,
        int prob, double maxWeight, double step
    ) {
        // initialize variables needed for searching
        Tuple<Integer, Integer> start = new Tuple<>(0, 0);
        Tuple<Integer, Integer> end = new Tuple<>(xDim-1, yDim-1);
        SearchAlgo aStar = new AStarSearch(Heuristics::manhattanDistance); // used to test for solvability

        ArrayList<GridWorldInfo> results = new ArrayList<>(); // for regular A*
        ArrayList<GridWorldInfo> repeatedResults = new ArrayList<>(); // for repeated A*
        for(int iter = 0; iter < numIter; ++iter) {
            // keep generating grids until a solvable one is found
            Grid grid = new Grid(xDim, yDim, prob);
            while(aStar.search(start, end, grid, cell -> cell.isBlocked()).getPath() == null) {
                grid = new Grid(xDim, yDim, prob);
            }

            for(int i = 0; 1.0 + i*step <= maxWeight; ++i) {
                // create search object using weighted heuristic
                double weight = 1.0 + i*step;
                SearchAlgo weightedAStar = new AStarSearch((coord1, coord2) -> weight*heuristic.apply(coord1, coord2));

                // run regular A* with the weighted heuristic and record the result
                long startTime = System.nanoTime();
                GridWorldInfo result = weightedAStar.search(start, end, grid, cell -> cell.isBlocked());
                long endTime = System.nanoTime();
                result.setProbability(prob);
                result.setWeight(weight);
                result.setRuntime(endTime - startTime);
                results.add(result);

                // run repeated A* with the weight heuristic snd record the result
                Robot robot = new Robot(start, end, true, grid, weightedAStar);
                startTime = System.nanoTime();
                GridWorldInfo repeatedResult = robot.run();
                endTime = System.nanoTime();
                repeatedResult.setProbability(prob);
                repeatedResult.setWeight(weight);
                repeatedResult.setRuntime(endTime - startTime);
                repeatedResults.add(repeatedResult);
            }
        }

        printResultsToCsv(fileName1, results);
        printResultsToCsv(fileName2, repeatedResults);
    }

}
