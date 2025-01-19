package chess;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public int[][] moveDirections() {
        return new int[][]{
                {1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},
                {1,-1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},
                {-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},
                {-1,-1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},
        };
    }
}