package chess;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public int[][] moveDirections() {
        return new int[][] {
                {0,1},
                {0,-1},
                {1,0},
                {-1,0}
        };
    }

    @Override
    public boolean canMoveALot() {
        return true;
    }
}
