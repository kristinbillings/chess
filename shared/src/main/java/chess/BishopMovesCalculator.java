package chess;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public int[][] moveDirections() {
        return new int[][]{
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };
    }
}