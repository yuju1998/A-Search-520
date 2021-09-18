import java.util.List;

public class GridWorldInfo {
    double probability;
    double trajectoryLength;
    int numberOfCellsProcessed;
    List<Tuple<Integer, Integer>> path; // path does not include start cell

    public GridWorldInfo(double trajectoryLength, int numberOfCellsProcessed, List<Tuple<Integer, Integer>> path) {
        this.trajectoryLength = trajectoryLength;
        this.numberOfCellsProcessed = numberOfCellsProcessed;
        this.path = path;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getTrajectoryLength() {
        return trajectoryLength;
    }

    public int getNumberOfCellsProcessed() {
        return numberOfCellsProcessed;
    }

    public List<Tuple<Integer, Integer>> getPath() {
        return path;
    }

}
