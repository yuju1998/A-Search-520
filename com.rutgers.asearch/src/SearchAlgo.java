@FunctionalInterface
public interface SearchAlgo {
    GridWorldInfo search(Tuple<Integer, Integer> start, Tuple<Integer, Integer> end, Grid grid, HashSet<GridCell> blocked);
}
