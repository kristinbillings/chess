package chess;

public class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public int[][] moveDirections() {
        return new int[][] {
                {2,1},
                {2,-1},
                {-2,1},
                {-2,-1},
                {1,2},
                {1,-2},
                {-1,2},
                {-1,-2}

        };
    }
}
