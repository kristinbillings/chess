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
    //executes stuff called by client


    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private static Random rand = new Random();

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

        if (Objects.equals(playerColor, "WHITE")) {
            playerColor = "es";
            drawRowOfSquares(out, squares);
        } else if (Objects.equals(playerColor, "BLACK")) {
            playerColor = "es";
        }


    }

    private static void drawHeaders(PrintStream out, String playerColor) {
        String[] headers = {};

        if (Objects.equals(playerColor, "WHITE")) {
            headers = new String[]{"    ","a", "b", "c", "d", "e", "f", "g", "h","   "};
        } else if (Objects.equals(playerColor, "BLACK")) {
            headers = new String[]{"    ","h", "g", "f", "e", "d","c","b","a","   "};
        }

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        //out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        //out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawRowOfSquares(PrintStream out, ChessPiece[][] squares) {
        for (int squareRow = 0; squareRow < BOARD_SIZE_IN_SQUARES; ++squareRow) {
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
            printHeaderText(out, String.valueOf(squareRow+1));
            out.print(EMPTY.repeat(1));

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setBlack(out);

                if (squares[squareRow][boardCol] == null) {
                    out.print(EMPTY.repeat(1));
                } else {
                    ChessPiece.PieceType pieceType = squares[squareRow][boardCol].getPieceType();
                    ChessGame.TeamColor pieceColor = squares[squareRow][boardCol].getTeamColor();
                    printPlayer(out, pieceType, pieceColor);
                    //out.print(EMPTY.repeat(1));
                }

                //if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw vertical column separator.
                  //  setRed(out);
                    //out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                //}

                setBlack(out);
            }
            //setRed(out);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
            printHeaderText(out, String.valueOf(squareRow+1));
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static void printPlayer(PrintStream out, ChessPiece.PieceType pieceType, ChessGame.TeamColor pieceColor) {
        if(pieceColor == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_LIGHT_GREY);
            out.print(K);
        } else {
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print(K);
        }
        //out.print(SET_BG_COLOR_WHITE);
        //out.print(SET_TEXT_COLOR_BLACK);
        //setWhite(out);
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
