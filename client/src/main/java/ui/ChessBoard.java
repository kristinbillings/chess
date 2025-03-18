package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.ERASE_SCREEN;

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
        if (Objects.equals(playerColor, "WHITE")) {
            playerColor = "es";
        } else if (Objects.equals(playerColor, "BLACK")) {

        }

    }

    private static void drawHeader(PrintStream out) {

    }

    private static void drawRowOfSquares(PrintStream out) {

    }

    private static void drawVerticalLine(PrintStream out) {

    }
}
