package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board = new ChessBoard();
    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        board.resetBoard();
    }

    private ChessPosition wheresKing(TeamColor teamColor) {
        ChessPosition kingPosition = null;

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                if(board.getPiece(position) != null) {
                    ChessGame.TeamColor spaceColor = board.getPiece(position).getTeamColor();
                    if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING && spaceColor == teamColor) {
                        kingPosition = position;
                    }
                }
            }
        }
        return kingPosition;
    }

    //returns false if the king is not safe
    private boolean isKingSafe(ChessPosition kingPosition, TeamColor teamColor){
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i,j);
                if(board.getPiece(position) != null) {
                    ChessGame.TeamColor spaceColor = board.getPiece(position).getTeamColor();
                    ChessPiece.PieceType type = board.getPiece(position).getPieceType();

                    if (spaceColor != teamColor) {
                        Collection<ChessMove> possibleMoves = new ChessPiece(spaceColor,type).pieceMoves(getBoard(),position);

                        if (possibleMoves.contains(new ChessMove(position,kingPosition,null))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    //returns false if king not safe
    private boolean checkKingMoves(ChessPosition kingPosition, ChessGame.TeamColor teamColor, ChessBoard boardCopy) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i,j);
                if(boardCopy.getPiece(position) != null) {
                    ChessGame.TeamColor spaceColor = boardCopy.getPiece(position).getTeamColor();
                    ChessPiece.PieceType type = boardCopy.getPiece(position).getPieceType();

                    if (spaceColor != teamColor) {
                        Collection<ChessMove> possibleMoves = new ChessPiece(spaceColor,type).pieceMoves(getBoard(),position);

                        if (possibleMoves.contains(new ChessMove(position,kingPosition,null))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        TeamColor currentColor = getBoard().getPiece(startPosition).getTeamColor();
        ChessPiece.PieceType type = getBoard().getPiece(startPosition).getPieceType();

        //if (type == null) {
          //  return null;
        //}

        Collection<ChessMove> possibleMoves = new ChessPiece(currentColor,type).pieceMoves(getBoard(),startPosition);

        for(ChessMove move : possibleMoves) {
            //change to the move

            ChessPiece ogStart = board.getPiece(move.getStartPosition());
            ChessPiece ogEnd = board.getPiece(move.getEndPosition());
            //change to the move
            ChessPiece.PieceType pieceType = move.getPromotionPiece();
            if (pieceType == null) {
                pieceType = board.getPiece(move.getStartPosition()).getPieceType();
            }
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), pieceType));

            if(type != ChessPiece.PieceType.KING) {
                if (!isInCheck(currentColor) && !isInCheckmate(currentColor)) {
                    validMoves.add(move);
                }
            } else if (checkKingMoves(move.getEndPosition(),currentColor,board)) {
                validMoves.add(move);
            }

            //change back to where it was
            board.addPiece(move.getStartPosition(), ogStart);
            board.addPiece(move.getEndPosition(), ogEnd);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //start position blank
        if(board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        //check to see that team colors match
        if(getTeamTurn() != board.getPiece(move.getStartPosition()).getTeamColor()){
            throw new InvalidMoveException("Wrong color");
        }

        //now creates possible moves after checking that everything is valid
        Collection<ChessMove> moves = validMoves(move.getStartPosition());

        if (moves.contains(move)) {
            ChessPiece.PieceType pieceType = move.getPromotionPiece();
            if (pieceType == null) {
                pieceType = board.getPiece(move.getStartPosition()).getPieceType();
            }

            board.addPiece(move.getStartPosition(),null);
            board.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), pieceType));
        } else {
            throw new InvalidMoveException("Move not valid");
        }

        //sets the turn to the other team
        if (getTeamTurn() == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = wheresKing(teamColor);
        if(isKingSafe(kingPosition,teamColor)){
            return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = wheresKing(teamColor);

        //check if other piece can block path or king can move
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);

                if(board.getPiece(position) != null) {
                    ChessGame.TeamColor spaceColor = board.getPiece(position).getTeamColor();
                    ChessPiece.PieceType type = board.getPiece(position).getPieceType();

                    if (spaceColor == teamColor) {
                        Collection<ChessMove> possibleMoves = new ChessPiece(spaceColor,type).pieceMoves(getBoard(),position);
                        for(ChessMove move : possibleMoves) {
                            //deepcopy board
                            ChessBoard boardCopy = new ChessBoard().clone();
                            boardCopy.addPiece(move.getStartPosition(), null);
                            boardCopy.addPiece(move.getEndPosition(), new ChessPiece(spaceColor, type));
                            //ckeck to see if the king is safe when other piece moves
                            if(type != ChessPiece.PieceType.KING && isKingSafe(kingPosition, teamColor)) {
                                return false;
                            //check to see if king safe when king moves
                            } else if(type == ChessPiece.PieceType.KING && !checkKingMoves(move.getEndPosition(),teamColor,boardCopy)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPosition = wheresKing(teamColor);

        if(!isInCheck(teamColor)) {
            //but no legal moves, aka all moves put time into check
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition position = new ChessPosition(i,j);
                    if(board.getPiece(position) != null) {
                        ChessGame.TeamColor spaceColor = board.getPiece(position).getTeamColor();
                        ChessPiece.PieceType type = board.getPiece(position).getPieceType();

                        if (spaceColor == teamColor) {
                            Collection<ChessMove> possibleMoves = new ChessPiece(spaceColor, type).pieceMoves(getBoard(), position);
                            for (ChessMove move : possibleMoves) {
                                //deepcopy board
                                ChessBoard boardCopy = new ChessBoard().clone();
                                boardCopy.addPiece(move.getStartPosition(), null);
                                boardCopy.addPiece(move.getEndPosition(), new ChessPiece(spaceColor, type));
                                //ckeck to see if the king is safe when other piece moves
                                if (type != ChessPiece.PieceType.KING && isKingSafe(kingPosition, teamColor)) {
                                    return false;
                                    //\check to see if king safe when king moves
                                } else if(type == ChessPiece.PieceType.KING && !checkKingMoves(move.getEndPosition(),teamColor,boardCopy)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

}
