package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

import ui.ChessBoard;
import net.ServerFacade;
import chess.ChessPiece;


public class Client {
    //draws menu and selects input*****
    //then calls methods in chessboard
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public Client(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }





    private static void drawChessBoard() {
        //takes in the board (will be the one that comes from the game
        ChessBoard.drawChessBoard("WHITE",new ChessPiece[8][8]); //change this later to be a variable passed in
    }

    public String PreloginMenu() {
        return """
                Welcome to chess!
                                
                - login         login to play
                - register      register to begin playing
                - quit          exits chess program
                - help          lists options
                """;
    }

    public String PostloginMenu() {
        return """
                What would you like to do?
                            
                - create        create a new game
                - list          list of all the games currently open
                - play          join a game and play
                - observe       watch a game
                - logout        logout of chess
                - help          lists options
                """;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return PreloginMenu();
        }
        return PostloginMenu();
    }

}
