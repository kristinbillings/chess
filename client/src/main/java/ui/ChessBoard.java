package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.ERASE_SCREEN;

public class ChessBoard {
    //executes stuff called by client


    public static void drawChessBoard(String playerColor) {
        //draws it in a certain orientation depending on if white, black, or observer(white)
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        if (Objects.equals(playerColor, "WHITE")) {
            playerColor = "es";
        }

    }

    private static void drawHeader(PrintStream out) {

    }

    private static void drawRowOfSquares(PrintStream out) {

    }

    private static void drawVerticalLine(PrintStream out) {

    }
}
