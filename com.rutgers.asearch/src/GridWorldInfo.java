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

    public void setTrajectoryLength(double trajectoryLength) {
        this.trajectoryLength = trajectoryLength;
    }

    public void setNumberOfCellsProcessed(int numberOfCellsProcessed) {
        this.numberOfCellsProcessed = numberOfCellsProcessed;
    }

    public void setPath(List<Tuple<Integer, Integer>> path) {
        this.path = path;
    }

    public List<Tuple<Integer, Integer>> getPath() {
        return path;
    }

    public void addCellsProcessed(int additionalCells) {
        this.numberOfCellsProcessed += additionalCells;
    }

    public void addTrajectoryLength(double additionTrajectory) {
        this.trajectoryLength += additionTrajectory;
    }
}
