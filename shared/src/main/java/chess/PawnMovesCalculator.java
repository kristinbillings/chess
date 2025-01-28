package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    //returns a collection of 4 chess moves that are the same except for the promotion piece
    private Collection<ChessMove> promotions(ChessPosition start, ChessPosition end) {
        Collection<ChessMove> pieceMoves = new ArrayList<>();

        pieceMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        pieceMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        pieceMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        pieceMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));

        return pieceMoves;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMoves = new ArrayList<>();
        ChessGame.TeamColor currentColor = board.getPiece(myPosition).getTeamColor();

        //initial/first time movements
        if (currentColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) {
            if (checkIfEmpty(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()))) {
                ChessPosition endPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if (checkIfEmpty(board, endPosition)) {
                    if(endPosition.getRow() == 8) { //for promotions
                        pieceMoves.addAll(promotions(myPosition,endPosition));
                    } else {
                        pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }
            }
        } else if (currentColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7) {
            if (checkIfEmpty(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()))) {
                ChessPosition endPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if (checkIfEmpty(board, endPosition)) {
                    if(endPosition.getRow() == 1) { //for promotions
                        pieceMoves.addAll(promotions(myPosition,endPosition));
                    } else {
                        pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                    }                }
            }
        }

        //normal movements
        if (currentColor == ChessGame.TeamColor.WHITE) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if(checkIfOnBoard(endPosition) && checkIfEmpty(board, endPosition)) {
                if(endPosition.getRow() == 8) { //for promotions
                    pieceMoves.addAll(promotions(myPosition,endPosition));
                } else {
                    pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                }            }
        } else if (currentColor == ChessGame.TeamColor.BLACK) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if(checkIfOnBoard(endPosition) && checkIfEmpty(board, endPosition)) {
                if(endPosition.getRow() == 1) { //for promotions
                    pieceMoves.addAll(promotions(myPosition,endPosition));
                } else {
                    pieceMoves.add(new ChessMove(myPosition, endPosition, null));
                }            }
        }

        //Capturing movements
        if (currentColor == ChessGame.TeamColor.WHITE) {
            ChessPosition endPosition1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+1);
            ChessPosition endPosition2 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()-1);
            if(checkIfOnBoard(endPosition1) && !checkIfEmpty(board, endPosition1) && checkOppositeColor(board,endPosition1, currentColor)){
                if(endPosition1.getRow() == 8) { //for promotions
                    pieceMoves.addAll(promotions(myPosition,endPosition1));
                } else {
                    pieceMoves.add(new ChessMove(myPosition, endPosition1, null));
                }            }
            if(checkIfOnBoard(endPosition2) && !checkIfEmpty(board, endPosition2) && checkOppositeColor(board,endPosition2, currentColor)){
                if(endPosition2.getRow() == 8) { //for promotions
                    pieceMoves.addAll(promotions(myPosition,endPosition2));
                } else {
                    pieceMoves.add(new ChessMove(myPosition, endPosition2, null));
                }            }
        } else if (currentColor == ChessGame.TeamColor.BLACK) {
            ChessPosition endPosition1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()+1);
            ChessPosition endPosition2 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()-1);
            if(checkIfOnBoard(endPosition1) && !checkIfEmpty(board, endPosition1) && checkOppositeColor(board,endPosition1, currentColor)){
                if(endPosition1.getRow() == 1) { //for promotions
                    pieceMoves.addAll(promotions(myPosition,endPosition1));
                } else {
                    pieceMoves.add(new ChessMove(myPosition, endPosition1, null));
                }            }
            if(checkIfOnBoard(endPosition2) && !checkIfEmpty(board, endPosition2) && checkOppositeColor(board,endPosition2, currentColor)){
                if(endPosition2.getRow() == 1) { //for promotions
                    pieceMoves.addAll(promotions(myPosition,endPosition2));
                } else {
                    pieceMoves.add(new ChessMove(myPosition, endPosition2, null));
                }
            }
        }

        return pieceMoves;
    }
}
