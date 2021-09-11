public class Main {


    public double euclideanDistance(int currentX, int currentY, int goalX, int goalY){
        double tempX = Math.pow(currentX - goalX,2);
        double tempY = Math.pow(currentY - goalY,2);

        return Math.sqrt(tempX+tempY);
    }

    public double manhattanDistance(int currentX, int currentY, int goalX, int goalY){
        double tempX = Math.abs(currentX - goalX);
        double tempY = Math.abs(currentY - goalY);

        return tempX + tempY;
    }

    public double chebyshevDistance(int currentX, int currentY, int goalX, int goalY){
        double tempX = Math.abs(currentX - goalX);
        double tempY = Math.abs(currentY - goalY);

        return Math.max(tempX , tempY);
    }

    public static void main(String[] args) {
        Grid grid = new Grid(10,10,10);
        System.out.println(grid);
    }
}
