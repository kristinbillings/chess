package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoard {
    //executes stuff called by client


    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String K = " K ";
    private static final String Q = " Q ";
    private static final String R = " R ";
    private static final String B = " B ";
    private static final String N = " N ";
    private static final String P = " P ";



    public static void drawChessBoard(String playerColor) {
        //draws it in a certain orientation depending on if white, black, or observer(white)
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawHeaders(out,playerColor);

        if (Objects.equals(playerColor, "WHITE")) {
            playerColor = "es";
        } else if (Objects.equals(playerColor, "BLACK")) {

        }

    }

    private static void drawHeaders(PrintStream out, String playerColor) {
        String[] headers = {};

        if (Objects.equals(playerColor, "WHITE")) {
            headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        } else if (Objects.equals(playerColor, "BLACK")) {
            headers = new String[]{"h", "g", "f", "e", "d","c","b","a"};
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

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawRowOfSquares(PrintStream out) {

    }

    private static void drawVerticalLine(PrintStream out) {

    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
