package ui;

import exceptions.ResponseException;
import net.ServerFacade;

import java.util.Arrays;

public class GamePlay {
    private final String serverUrl;
    private ServerFacade serverFacade;

    public GamePlay(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluate(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw(params);
                case "leave" -> leave(params);
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "quit" -> "quit";
                default -> help(cmd);
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String redraw(String... params) throws ResponseException {
        if (params.length == 0) {
            return ("\tThis is where the board will be redrawn\n\n");
        }
        throw new ResponseException(400, "Not redrawing board\n");
    }

    private String leave(String... params) throws ResponseException {
        if (params.length == 0) {
            return ("\tThis is where the user is removed from the game\n\n");
        }
        throw new ResponseException(400, "Did not remove user\n");
    }

    private String move(String... params) throws ResponseException {
        if (params.length == 0) {
            return ("\tThis is where the user's move is executed'\n\n");
        }
        throw new ResponseException(400, "Piece did not move\n");
    }

    private String resign(String... params) throws ResponseException {
        if (params.length == 0) {
            return ("\tThis is where the user resigns'\n\n");
        }
        throw new ResponseException(400, "Did not resign\n");
    }

    private String help(String cmd) throws ResponseException {
        return (
                """
                Possible Actions:
                - redraw  --  redraws game board
                - leave  --  removes you from the game
                - move <> <>  --  makes a move on your board
                - resign  --  forfeits and ends game
                - help  --  lists possible actions
                - quit  --  quit playing chess
                 """);
    }
}
