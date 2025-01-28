package chess;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public int[][] moveDirections() {
        return new int[][]{
                {-1,1},     //1
                {0,1},      //2
                {1,1},      //3
                {1,0},      //4
                {-1,0},     //5
                {-1,-1},    //6
                {0,-1},     //7
                {1,-1},     //8
        };
    }

    @Override
    public boolean canMoveALot() {
        return true;
    }
}
