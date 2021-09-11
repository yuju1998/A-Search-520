import java.util.ArrayList;

public class Grid {

    private ArrayList<ArrayList<GridCell>> grid;
    private int xSize;
    private int ySize;

    private boolean generateIsBlocked(int probabilityOfBlocked){
        return Math.random()*100 + 1 < probabilityOfBlocked;
    }

    private ArrayList<ArrayList<GridCell>> generateGrid(int dimensionX, int dimensionY, int probabilityOfBlocked){
        ArrayList<ArrayList<GridCell>> grid = new ArrayList<>();

        for (int x = 0; x< dimensionX; x++){
            ArrayList<GridCell> tempList = new ArrayList<>();
            for(int y = 0; y< dimensionY; y++){
                if((x == 0 && y == 0) || (x == dimensionX-1 && y == dimensionY-1)){
                    tempList.add(new GridCell(x,y,generateIsBlocked(0)));
                }
                else {
                    tempList.add(new GridCell(x, y, generateIsBlocked(probabilityOfBlocked)));
                }

            }
            grid.add(tempList);
        }

        return grid;
    }

    public Grid(int xSize, int ySize, int probability) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.grid = generateGrid(xSize,ySize, probability);
    }

    public GridCell getCell(int x, int y){
        if(x < xSize && y < ySize){
            return grid.get(x).get(y);
        }
        return null;
    }


    public ArrayList<ArrayList<GridCell>> getGrid() {
        return grid;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Grid{");
        for (int x = 0; x < xSize; x++){
            builder.append("\n");
            for(int y = 0; y< ySize; y++) {

                if (x == 0 && y == 0) {
                    builder.append("S");
                } else if (x == xSize - 1 && y == ySize - 1) {
                    builder.append("G");
                } else {
                    if (this.getCell(x, y).isBlocked()) {
                        builder.append("X");
                    } else {
                        builder.append(" ");
                    }
                }
                builder.append(",");
            }
        }
        builder.append("\n}");
        return builder.toString();
    }
}
