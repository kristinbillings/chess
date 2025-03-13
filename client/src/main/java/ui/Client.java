package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class Client {
    //draws menu and selects input
    //then calls method in chessboard
    private static void drawChessBoard(PrintStream out) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
    }
    private static void drawHeader(PrintStream out) {

    }

    private static void drawRowOfSquares(PrintStream out) {

    }

    private static void drawVerticalLine(PrintStream out) {

    }
}
