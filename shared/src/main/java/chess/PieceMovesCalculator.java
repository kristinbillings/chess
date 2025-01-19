package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PieceMovesCalculator {
    default int[][] moveDirections() {
        return null;
    }

    //tells whether the piece type can ignore other pieces in its way or not
    default boolean canJump() {
        return false;
    }

    default boolean canMoveALot() {
        return false;
    }

    private boolean checkIfSpaceOccupied(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        boolean isValid = false;

        if (position.getRow() >= 0 && position.getRow() <= 7 && position.getColumn() >= 0 && position.getColumn() <= 7) {
            if (board.getPiece(position) == null) {
                isValid = true;
            } else if (color != (board.getPiece(position)).getTeamColor()) {
                isValid = true;
            }
        }
        return isValid;
    }

    /**
     * checks whether the path of the piece is clear or obstructed
     * this is to see if a movement is valid or not
     */

    default public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMoves = new ArrayList<>();

        int[][] pieceMovements = moveDirections();

        for (int i = 0; i < pieceMovements.length; i++) {
            ChessPosition endPosition = new ChessPosition(pieceMovements[i][0] + myPosition.getRow(), pieceMovements[i][1] + myPosition.getColumn());
            //checks that the movement is on the board
            int bruh = pieceMovements[i][0];

            if (canMoveALot()) {

            } else {
                //check to see if position can be used
                ChessGame.TeamColor currentColor = (board.getPiece(myPosition)).getTeamColor();
                if (checkIfSpaceOccupied(board, endPosition, currentColor)) {
                    pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                }
                //if true add to pieceMoves

            }


        }
        return pieceMoves;
    }
}
