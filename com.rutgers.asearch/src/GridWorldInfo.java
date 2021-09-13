public class GridWorldInfo {
    double probability;
    double trajectoryLength;
    int numberOfCellsProcessed;

    public GridWorldInfo(double trajectoryLength, int numberOfCellsProcessed) {
        this.trajectoryLength = trajectoryLength;
        this.numberOfCellsProcessed = numberOfCellsProcessed;
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

}
