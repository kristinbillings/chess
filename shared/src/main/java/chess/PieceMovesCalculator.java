package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    default int[][] moveDirections() {
        return null;
    }

    //tells if the piece can move more square (Queen, Bishop, Tower)
    default boolean canMoveALot() {
        return false;
    }

    private boolean checkIfSpaceOccupiedOrValid(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        boolean isValid = false;

        if (position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8) {
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
            ChessGame.TeamColor currentColor = (board.getPiece(myPosition)).getTeamColor();

            if (canMoveALot()) {
                boolean stillGoing = true;
                while (stillGoing) {
                    if (checkIfSpaceOccupiedOrValid(board, endPosition, currentColor)) {
                        pieceMoves.add(new ChessMove(myPosition, endPosition, null));

                        //checks to see if captured piece, then also stops loop
                        if (board.getPiece(endPosition) != null) {
                            if (currentColor != (board.getPiece(endPosition)).getTeamColor()) {
                                stillGoing = false;
                            }
                        }
                        endPosition = new ChessPosition(pieceMovements[i][0] + endPosition.getRow(), pieceMovements[i][1] + endPosition.getColumn());
                    } else {
                        stillGoing = false;
                    }
                }
            } else {
                //check to see if position can be used
                if (checkIfSpaceOccupiedOrValid(board, endPosition, currentColor)) {
                    pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
        return pieceMoves;
    }
}
