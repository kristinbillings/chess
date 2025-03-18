package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;
import ui.ChessBoard;

public class Client {
    //draws menu and selects input*****
    //then calls methods in chessboard
    //ima just use the letters as the characters(this is less work)
    private static void drawChessBoard() {
        ChessBoard.drawChessBoard("WHITE"); //change this later to be a variable passed in
    }

}
