package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    default int[][] moveDirections(){
        return null;
    }

    default boolean canMoveALot() {
        return false;
    }

    default boolean checkIfOnBoard(ChessPosition position) {
        if (position.getRow() >=1 && position.getRow() <=8 && position.getColumn() >=1 && position.getColumn()<=8) {
            return true;
        } else {
            return false;
        }
    }

    default boolean checkIfEmpty(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition) == null) {
            return true;
        } else {
            return false;
        }
    }

    default boolean checkOppositeColor(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        if(board.getPiece(position).getTeamColor() == color) {
            return false;
        } else {
            return true;
        }
    }

    default Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMoves = new ArrayList<>();
        ChessGame.TeamColor currentColor = board.getPiece(myPosition).getTeamColor();
        int[][] possibleMovements = moveDirections();

        for (int i = 0; i < possibleMovements.length; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+possibleMovements[i][0], myPosition.getColumn()+possibleMovements[i][1]);
            boolean keepGoing = true;

            while(keepGoing) {
                keepGoing = canMoveALot();
                if (checkIfOnBoard(endPosition)) {
                    if (checkIfEmpty(board, endPosition)) {
                        pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                        endPosition = new ChessPosition(endPosition.getRow()+possibleMovements[i][0], endPosition.getColumn()+possibleMovements[i][1]);
                    } else if (checkOppositeColor(board, endPosition, currentColor)) {
                        pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                        keepGoing = false;
                    } else {
                        keepGoing = false;
                    }
                } else {
                    keepGoing = false;
                }
            }
        }


        return pieceMoves;
    }
}
