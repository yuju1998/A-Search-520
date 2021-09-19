import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    public static GridWorldInfo runRobot(Robot rob, Grid grid, SearchAlgo algo) {
        // statistics to be collected and returned
        double totalTrajectoryLength = 0;
        int totalCellsProcessed = 0;
        ArrayList<Tuple<Integer, Integer>> totalPath = new ArrayList<>();

        // while robot has not reached the destination
        while(rob.getLocation().f1.intValue() != rob.getGoal().f1.intValue() || rob.getLocation().f2.intValue() != rob.getGoal().f2.intValue()) {
            // find path
            GridWorldInfo result = algo.search(rob.getLocation(), rob.getGoal(), grid, rob.getKnownObstacles()::contains);
            totalCellsProcessed += result.getNumberOfCellsProcessed();

            // if no path found, exit with failure
            if(result.getPath() == null) {
                totalTrajectoryLength = Double.NaN;
                totalPath = null;
                break;
            }

            // else, attempt to move along that path, and update the overall path taken
            int stepsTaken = rob.run(result.getPath(), grid);
            totalTrajectoryLength += stepsTaken;
            totalPath.addAll(result.getPath().subList(0, stepsTaken));
        }

        // return output
        return new GridWorldInfo(totalTrajectoryLength, totalCellsProcessed, totalPath);
    }

    public static void runProbabilitySimulation(int xDimension, int yDimension, int numberOfIterations, boolean canSeeSideways) {
        ArrayList<GridWorldInfo> solutionDensity = new ArrayList<>();
        for (int i = 0; i <= 33; i++){
            for (int j = 0; j<numberOfIterations; j++) {
                Grid grid = new Grid(xDimension, yDimension, i);
                aStarSearchObject aso = new aStarSearchObject(Heuristics::euclideanDistance);
                Robot bot = new Robot(new Tuple<>(0, 0), new Tuple<>(xDimension - 1, yDimension - 1), canSeeSideways);
                GridWorldInfo info = runRobot(bot, grid, aso::aStarSearch);
                info.setProbability(i);
                solutionDensity.add(info);
            }
        }
        String bumps = canSeeSideways ? "SideFov" : "NoSideFov";
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
        //runProbabilitySimulation(x,y,iterations,false);
    }





}
