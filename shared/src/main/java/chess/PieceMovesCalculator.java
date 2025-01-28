package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    //the directions a piece can move
    default int[][] moveDirections(){
        return null;
    }

    //returns true if a piece move more than once
    default boolean canMoveALot() {
        return false;
    }

    //returns true if position is on the board ([8][8])
    default boolean checkIfOnBoard(ChessPosition position) {
        if (position.getRow() >=1 && position.getRow() <=8 && position.getColumn() >=1 && position.getColumn()<=8) {
            return true;
        } else {
            return false;
        }
    }

    //returns true if a space is empty, aka null
    default boolean checkIfEmpty(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition) == null) {
            return true;
        } else {
            return false;
        }
    }

    //returns true if the current piece color is different from the color of the piece at a position
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
            int newRow = myPosition.getRow()+possibleMovements[i][0];
            int newCol = myPosition.getColumn()+possibleMovements[i][1];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            boolean keepGoing = true;

            while(keepGoing) {
                keepGoing = canMoveALot();
                if (checkIfOnBoard(endPosition)) {
                    if (checkIfEmpty(board, endPosition)) {
                        pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                        int keepGoingNewRow = endPosition.getRow()+possibleMovements[i][0];
                        int keepGoingNewCol = endPosition.getColumn()+possibleMovements[i][1];
                        endPosition = new ChessPosition(keepGoingNewRow, keepGoingNewCol);
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
