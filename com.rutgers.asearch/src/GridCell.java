public class GridCell {
    private int x;
    private int y;
    private boolean isBlocked;
    private double cost;

    public GridCell(int x, int y, boolean isBlocked) {
        this.x = x;
        this.y = y;
        this.isBlocked = isBlocked;
        this.cost = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "GridCell{" +
                "x=" + x +
                ", y=" + y +
                ", isBlocked=" + isBlocked +
                ", cost=" + cost +
                '}';
    }
}
