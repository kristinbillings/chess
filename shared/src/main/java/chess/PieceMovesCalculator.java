package chess;

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

    default public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMoves = List.of();

        ChessPosition startP = new ChessPosition(2,4);

        int[][] pieceMovements = moveDirections();

        for (int i = 0; i < pieceMovements.length; i++) {
            ChessPosition possibleMove = new ChessPosition(pieceMovements[i][0] + myPosition.getRow(), pieceMovements[i][1] + myPosition.getColumn());
            //checks that the movement is on the board
            if (possibleMove.getRow() >= 1 && possibleMove.getRow() <= 8 && possibleMove.getColumn() >= 1 && possibleMove.getColumn() <= 8) {
                //checks to see if the position is empty
                if (board.getPiece(possibleMove) == null) {
                    //if the piece can jump, then immediately add piece
                    if (canJump() == ture) {
                        pieceMoves.add(new ChessMove(myPosition, possibleMove, null));
                    } else {
                    }
                    }
                } else {


                }
            }
        }

        pieceMoves.add(new ChessMove(startP, ));

        return null;
    }



}
