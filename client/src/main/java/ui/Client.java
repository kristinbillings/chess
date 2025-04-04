package ui;

import java.util.List;
import java.util.Scanner;

import net.ServerFacade;


public class Client {
    //gets input and then goes to pre- or post-login class
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private GameStatus gameStatus = GameStatus.OUTGAME;
    private Prelogin preloginState;
    private Postlogin postloginState;
    private GamePlay gamePlayState;

    public Client(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.preloginState = new Prelogin(serverUrl);

    }

    public void run() {
        System.out.print(preloginMenu());
        Scanner scanner = new Scanner(System.in);

        var result = "";
        while (!result.equals("quit")) {
            String userInput = scanner.nextLine();
            if (state == State.SIGNEDOUT) {
                result = preloginState.evaluate(userInput);
                System.out.print(result);
                if (result.contains("uccessful")) {
                    state = State.SIGNEDIN;
                    gameStatus = GameStatus.OUTGAME;
                    postloginState = new Postlogin(serverUrl);
                    System.out.print(postloginMenu());
                }
            } else if (state == State.SIGNEDIN && gameStatus == GameStatus.OUTGAME) {
                result = postloginState.evaluate(userInput, preloginState.getAuth());
                if (result.contains("Successfully logged out.")) {
                    state = State.SIGNEDOUT;
                    result += "\n\n" + preloginMenu();
                } else if (result.contains("joined") | result.contains("Observing")) {
                    gameStatus = GameStatus.INGAME;
                    gamePlayState = new GamePlay(serverUrl);
                    System.out.print(inGameMenu());
                    //bro
                }
                System.out.print(result);
            } else if (gameStatus == GameStatus.INGAME) {
                result = gamePlayState.evaluate(userInput);
                if (result.equals("quit")) {
                    gameStatus = GameStatus.OUTGAME;
                    System.out.print("\nLEAVING GAME\n\n");
                    result = "o";
                    System.out.print("Main Menu:" + postloginMenu());
                } else {
                    result = gamePlayState.evaluate(userInput);
                    System.out.print(result);
                    //result = "";
                    //System.out.print(inGameMenu());
                }
            }
        }
        System.out.print("\nGoodbye\n");
    }

    private static void drawChessBoard() {
        //takes in the board (will be the one that comes from the game
        Board.drawChessBoard("WHITE"); //change this later to be a variable passed in
    }

    public String preloginMenu() {
        return """
                Welcome to chess!           
                - login <USERNAME> <PASSWORD>  --  login to play
                - register <USERNAME> <PASSWORD> <EMAIL>  --  register to begin playing
                - quit  --  exits chess program
                - help  --  lists options
                
                Choose one:
                """;
    }

    public String postloginMenu() {
        return """
                \nWhat would you like to do?       
                - create <NAME>  --  create a new game
                - list  --  list of all the games currently open
                - join <ID> [WHITE|BLACK]  --  join a game and play
                - observe <ID>  --  watch a game
                - logout  --  logout of chess
                - help  --  lists options
                - quit  --  quit playing chess
                """;
    }

    public String inGameMenu() {
        return """
                \nPossible Actions:
                - redraw  --  redraws game board
                - leave  --  removes you from the game
                - move <> <>  --  makes a move on your board
                - resign  --  forfeits and ends game
                - help  --  lists possible actions
                - quit  --  quit playing chess
                 """;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return preloginMenu();
        }
        return postloginMenu();
    }

}
