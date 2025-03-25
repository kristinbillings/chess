package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoard {
    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;

    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String K = " K ";
    private static final String Q = " Q ";
    private static final String R = " R ";
    private static final String B = " B ";
    private static final String N = " N ";
    private static final String P = " P ";

    public static void drawChessBoard(String playerColor, ChessPiece[][] squares) {
        //draws it in a certain orientation depending on if white, black, or observer(white)
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawHeaders(out,playerColor);
        drawRowOfSquares(out, squares, playerColor);
        drawHeaders(out,playerColor);
    }

    private static void drawHeaders(PrintStream out, String playerColor) {
        String[] headers = {};

        if (Objects.equals(playerColor, "WHITE")) {
            headers = new String[]{"  a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        } else if (Objects.equals(playerColor, "BLACK")) {
            headers = new String[]{"  h ", " g ", " f ", " e ", " d "," c "," b "," a "};
        }

        out.print(EMPTY.repeat(2));
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(player);

        setBlack(out);
    }

    private static void drawRowOfSquares(PrintStream out, ChessPiece[][] squares, String playerColor) {
        int rowChange = 8;
        int sidey = 0;
        if (Objects.equals(playerColor, "BLACK")) {
            rowChange = 0;
            sidey = 1;
        }

        for (int squareRow = 0; squareRow < BOARD_SIZE_IN_SQUARES; ++squareRow) {
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
            printHeaderText(out, String.valueOf(Math.abs(squareRow-rowChange)+sidey));
            out.print(EMPTY.repeat(1));

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                int row = squareRow;
                int col = boardCol;
                if (rowChange == 0) {
                    row = Math.abs(row - 7);
                    col = Math.abs(col - 7);
                }

                setBlack(out);

                if(row%2 == 0 && col%2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else if (row%2 == 1 && col%2 == 1) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }

                if (squares[row][col] == null) {
                    out.print(EMPTY.repeat(1));
                } else {
                    ChessPiece.PieceType pieceType = squares[row][col].getPieceType();
                    ChessGame.TeamColor pieceColor = squares[row][col].getTeamColor();
                    printPlayer(out, pieceType, pieceColor);
                }
                setBlack(out);
            }
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
            printHeaderText(out, String.valueOf(Math.abs(squareRow-rowChange)+sidey));
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
            setBlack(out);

            out.println();
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, ChessPiece.PieceType pieceType, ChessGame.TeamColor pieceColor) {
        String x = "";
        switch(pieceType) {
            case ChessPiece.PieceType.KING -> x = K;
            case ChessPiece.PieceType.QUEEN -> x = Q;
            case ChessPiece.PieceType.BISHOP -> x = B;
            case ChessPiece.PieceType.KNIGHT -> x = N;
            case ChessPiece.PieceType.ROOK -> x = R;
            case ChessPiece.PieceType.PAWN -> x = P;
        }

        if(pieceColor == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(x);
        } else {
            out.print(SET_TEXT_COLOR_RED);
            out.print(x);
        }
    }


    //for right now, remove later
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }
    public void resetBoard() {
        /* make a new piece, us add piece to add it to a specific location
         */
        addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,2),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,3),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(2,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2,2),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2,3),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        addPiece(new ChessPosition(8,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,3),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,6),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,7),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(7,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7,3),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7,6),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7,7),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

    }

    //for checking my code while writing it
    void main() {
        resetBoard();
        drawChessBoard("WHITE", squares);
    }
}
