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

    /** checks whether the path of the piece is clear or obstructed
     * this is to see if a movement is valid or not
     */
    default public boolean isPathClear(ChessBoard board, ChessPosition myPosition, ChessPosition possibleMove) {
        boolean isPathClear = true;

        int changeCol = possibleMove.getColumn() - myPosition.getColumn();
        int changeRow = possibleMove.getRow() - myPosition.getRow();


        if (changeCol == 0) {
            if (changeRow > 0) {
                for (int j = 1; j < changeRow; j++) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + j, myPosition.getColumn())) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            } else {
                for (int j = -1; j > changeRow; j--) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + j, myPosition.getColumn())) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            }

        } else if (changeRow == 0) {
            if (changeCol > 0) {
                for (int j = 1; j < changeCol; j++) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            } else {
                for (int j = -1; j > changeCol; j--) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            }
        } else {
            if (changeRow > 0 && changeCol > 0) {
                for (int j = 1; j < changeRow; j++) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + j, myPosition.getColumn() + j)) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            } else if (changeRow > 0 && changeCol < 0) {
                for (int j = 1; j < changeRow; j++) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + j, myPosition.getColumn() - j)) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            } else if (changeRow < 0 && changeCol > 0) {
                for (int j = 1; j < changeCol; j++) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - j, myPosition.getColumn() + j)) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            } else {
                for (int j = -1; j > changeCol; j--) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + j, myPosition.getColumn() + j)) != null) {
                        isPathClear = false;
                        break;
                    }
                }
            }
        }
        return isPathClear;
    }


    default public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMoves = new java.util.ArrayList<>(List.of());
                                            //intellij told me to change this^^???
        int[][] pieceMovements = moveDirections();

        for (int i = 0; i < pieceMovements.length; i++) {
            ChessPosition possibleMove = new ChessPosition(pieceMovements[i][0] + myPosition.getRow(), pieceMovements[i][1] + myPosition.getColumn());
            //checks that the movement is on the board
            if (possibleMove.getRow() >= 0 && possibleMove.getRow() <= 7 && possibleMove.getColumn() >= 0 && possibleMove.getColumn() <= 7) {
                //checks to see if the position is empty
                if (board.getPiece(possibleMove) == null) {
                    //if the piece can jump (knight), then immediately add position
                    if (canJump()) {
                        pieceMoves.add(new ChessMove(myPosition, possibleMove, null));
                    } else { //checks to see if other pieces in the way of movement
                        if (isPathClear(board, myPosition, possibleMove)) {
                            pieceMoves.add(new ChessMove(myPosition, possibleMove, null));
                        }
                    }
                } else { //checks to see if space occupied by other player's piece
                    ChessGame.TeamColor currentType = (board.getPiece(myPosition)).getTeamColor();
                    ChessGame.TeamColor typeAtNewLocation = (board.getPiece(possibleMove)).getTeamColor();

                    if (currentType != typeAtNewLocation) {
                        if (canJump()) {
                            pieceMoves.add(new ChessMove(myPosition, possibleMove, null));
                        } else if (isPathClear(board,myPosition,possibleMove)) {
                            pieceMoves.add(new ChessMove(myPosition, possibleMove, null));
                        }
                    }
                }
            }
        }
        return pieceMoves;
    }



}
