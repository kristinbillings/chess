package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    private boolean checkIfSpaceOccupiedOrInBoard(ChessBoard board, ChessPosition position) {
        boolean isValid = false;

        if (position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8) {
            if (board.getPiece(position) == null) {
                isValid = true;
            }
        }
        return isValid;
    }

    private boolean checkOppositeColorAndValid(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        boolean isValid = false;
        if (position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8) {
            if (board.getPiece(position) != null) {
                if (color != (board.getPiece(position)).getTeamColor()) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        ChessGame.TeamColor currentColor = board.getPiece(myPosition).getTeamColor();

        //normal movement
        int[][] normalMovements = {{0,0}};
        if (currentColor == ChessGame.TeamColor.WHITE) {
            normalMovements[0][0] = 1;
        } else {
            normalMovements[0][0] = -1;
        }

        ChessPosition normalEnd = new ChessPosition(normalMovements[0][0] + myPosition.getRow(), normalMovements[0][1] + myPosition.getColumn());
        if (checkIfSpaceOccupiedOrInBoard(board,normalEnd)) {
            pieceMoves.add(new ChessMove(myPosition, normalEnd, null));
        }


        //beginning/first-time movement
        int[][] firstMovements = {{0,0}};
        if (currentRow == 2 && currentColor == ChessGame.TeamColor.WHITE) {
            firstMovements[0][0] = 2;
        } else if (currentRow == 7 && currentColor == ChessGame.TeamColor.BLACK) {
            firstMovements[0][0] = -2;
        }

        ChessPosition firstEnd = new ChessPosition(firstMovements[0][0] + myPosition.getRow(), firstMovements[0][1] + myPosition.getColumn());
        if (checkIfSpaceOccupiedOrInBoard(board,normalEnd) && checkIfSpaceOccupiedOrInBoard(board,firstEnd)) {
            pieceMoves.add(new ChessMove(myPosition, firstEnd, null));
        }


        //capturing
        int[][] captureMovements = {{0,0}, {0,0}};
        if(currentColor == ChessGame.TeamColor.WHITE) { //rows go up
            captureMovements[0][0] = 1; //row
            captureMovements[0][1] = 1;
            captureMovements[1][0] = 1; //row
            captureMovements[1][1] = -1;
        } else {                                        //BLACK, so rows go down
            captureMovements[0][0] = -1; //row
            captureMovements[0][1] = 1;
            captureMovements[1][0] = -1; //row
            captureMovements[1][1] = -1;
        }

        ChessPosition captureEnd1 = new ChessPosition(captureMovements[0][0] + myPosition.getRow(), captureMovements[0][1] + myPosition.getColumn());
        ChessPosition captureEnd2 = new ChessPosition(captureMovements[1][0] + myPosition.getRow(), captureMovements[1][1] + myPosition.getColumn());

        if(checkOppositeColorAndValid(board, captureEnd1, currentColor)) {
            pieceMoves.add(new ChessMove(myPosition, captureEnd1, null));
        }
        if(checkOppositeColorAndValid(board, captureEnd2, currentColor)) {
            pieceMoves.add(new ChessMove(myPosition, captureEnd2, null));
        }

        return pieceMoves;
    }

}
