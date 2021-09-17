public class Heuristics {

    public static double euclideanDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
        double tempX = Math.pow(currentCoordinate.f1 - goalCoordinate.f1, 2);
        double tempY = Math.pow(currentCoordinate.f2 - goalCoordinate.f2, 2);

        return Math.sqrt(tempX + tempY);
    }

    public static double manhattanDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
        double tempX = Math.abs(currentCoordinate.f1 - goalCoordinate.f1);
        double tempY = Math.abs(currentCoordinate.f2 - goalCoordinate.f2);

        return tempX + tempY;
    }

    public static double chebyshevDistance(Tuple<Integer, Integer> currentCoordinate, Tuple<Integer, Integer> goalCoordinate) {
        double tempX = Math.abs(currentCoordinate.f1 - goalCoordinate.f1);
        double tempY = Math.abs(currentCoordinate.f2 - goalCoordinate.f2);

        return Math.max(tempX, tempY);
    }

}
