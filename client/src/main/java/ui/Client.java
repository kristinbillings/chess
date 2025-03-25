package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

import ui.ChessBoard;
import net.ServerFacade;
import chess.ChessPiece;


public class Client {
    //draws menu and selects input*****
    //then calls methods in chessboard
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private Prelogin preloginState;
    private Postlogin postloginState;
    private String authToken;

    public Client(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.preloginState = new Prelogin(serverUrl);

    }

    public void run() {
        System.out.print(PreloginMenu());
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        if (state == State.SIGNEDOUT) {
            String result = preloginState.evaluate(userInput);
            System.out.print(result);
        } else if (state == State.SIGNEDIN) {
            String result = postloginState.evaluate(userInput, preloginState.getAuth());
            if (Objects.equals(result, "quit")) {

            }
            System.out.print(result);

        }


        //Scanner scanner = scanner(in)
    }




    private static void drawChessBoard() {
        //takes in the board (will be the one that comes from the game
        ChessBoard.drawChessBoard("WHITE",new ChessPiece[8][8]); //change this later to be a variable passed in
    }

    public String PreloginMenu() {
        return """
                Welcome to chess!
                                
                - login <USERNAME> <PASSWORD>  --  login to play
                - register <USERNAME> <PASSWORD> <EMAIL>  --  register to begin playing
                - quit  --  exits chess program
                - help  --  lists options
                
                Choose one:
                """;
    }

    public String PostloginMenu() {
        return """
                What would you like to do?
                            
                - create <NAME>  --  create a new game
                - list  --  list of all the games currently open
                - join  --  join a game and play
                - observe  <ID>  --  watch a game
                - logout  --  logout of chess
                - help  --  lists options
                - quit  --  quit playing chess
                """;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return PreloginMenu();
        }
        return PostloginMenu();
    }

}
