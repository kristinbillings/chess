package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class Board {
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

    //just here for this phase
    private static ChessBoard squares = new ChessBoard();


    public static void drawChessBoard(String playerColor) {
        //removed the variable to pass in just for this phase
        // it was this -->      , ChessPiece[][] squares

        squares.resetBoard();

        //draws it in a certain orientation depending on if white, black, or observer(white)
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawHeaders(out,playerColor);
        drawRowOfSquares(out, squares, playerColor);
        drawHeaders(out,playerColor);
        out.print(ERASE_SCREEN);

        out.print(RESET_TEXT_COLOR);
    }

    private static void drawHeaders(PrintStream out, String playerColor) {
        String[] headers = {};

        if (Objects.equals(playerColor, "WHITE")) {
            headers = new String[]{"  a ", " b ", " c ", " d ", " e ", " f ", " g ", " h  "};
        } else if (Objects.equals(playerColor, "BLACK")) {
            headers = new String[]{"  h ", " g ", " f ", " e ", " d "," c "," b "," a  "};
        }
        setBlack(out);
        out.print(EMPTY.repeat(2));
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(EMPTY.repeat(2));
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(player);
    }

    private static void drawRowOfSquares(PrintStream out, ChessBoard squares, String playerColor) {
        int rowChange = 8;
        int sidey = 0;
        if (Objects.equals(playerColor, "BLACK")) {
            rowChange = 0;
            sidey = 1;
        }

        for (int squareRow = 1; squareRow < BOARD_SIZE_IN_SQUARES+1; ++squareRow) {
            setBlack(out);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));

            int addNum = -1;
            if (rowChange == 8) {
                addNum = 1;
            }
            int number = Integer.valueOf(String.valueOf(Math.abs(squareRow-rowChange)+sidey)) +addNum;

            printHeaderText(out, String.valueOf(number));
            out.print(EMPTY.repeat(1));

            for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES+1; ++boardCol) {
                int row = squareRow;
                int col = boardCol;
                if (rowChange == 0) {
                    row = Math.abs(row - 9);
                    col = Math.abs(col - 9);
                }

                if(row%2 == 0 && col%2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else if (row%2 == 1 && col%2 == 1) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }

                if (squares.getPiece(new ChessPosition(row,col)) == null) {
                    out.print(EMPTY.repeat(1));
                } else {
                    ChessPiece.PieceType pieceType = squares.getPiece(new ChessPosition(row,col)).getPieceType();
                    ChessGame.TeamColor pieceColor = squares.getPiece(new ChessPosition(row,col)).getTeamColor();
                    printPlayer(out, pieceType, pieceColor);
                }
                setBlack(out);
            }
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));

            number = Integer.valueOf(String.valueOf(Math.abs(squareRow-rowChange)+sidey)) +addNum;

            printHeaderText(out, String.valueOf(number));
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
            out.print(RESET_BG_COLOR);

            //setBlack(out);

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
    //for checking my code while writing it
    void main() {
        //resetBoard();
        drawChessBoard("WHITE");
    }
}
