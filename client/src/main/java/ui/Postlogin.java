package ui;

import chess.ChessPiece;
import dataaccess.ResponseException;
import net.ServerFacade;
import requests.*;
import results.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Postlogin {
    private final String serverUrl;
    private ServerFacade serverFacade;
    private List<GameResult> currentGames = new ArrayList<>();

    public Postlogin(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluate(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(authToken,params);
                case "list" -> list(authToken, params);
                case "join" -> join(authToken, params);
                case "observe" -> observe(params);
                case "logout" -> logout(authToken,params);
                case "quit" -> "quit";
                default -> help(cmd);
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String create(String authToken, String... params) throws ResponseException {
        if (params.length == 1) {
            var gameName = params[0];
            CreateRequest request = new CreateRequest(gameName,authToken);
            CreateResult result = serverFacade.create(request);

            return ("Successfully created a game called: " + gameName);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    private String list(String authToken, String... params) throws ResponseException {
        if (params.length == 0) {
            ListRequest request = new ListRequest(authToken);
            ListResult result = serverFacade.list(request);

            String output = "Current Games: \n\n";
            currentGames.clear(); //resets so number are consistent

            for (int i = 0; i < result.games().size(); i++) {
                GameResult game = result.games().get(i);
                currentGames.add(game);
                output = output + (i+1)
                        + "\t" + game.gameName()
                        + ", White: " + game.whiteUsername()
                        + ", Black: " + game.blackUsername()
                        + "\n";
            }
            return output;
        }
        throw new ResponseException(400, "Expected: nothing after \"list\"");
    }

    private String join(String authToken,String... params) throws ResponseException {
        if (params.length == 2) {
            var gameNumber = Integer.parseInt(params[0]);
            var color = params[1];

            if (!Objects.equals(color, "WHITE") && !Objects.equals(color, "BLACK")) {
                throw new ResponseException(400, "Expected: [WHITE|BLACK]");
            }
            if (gameNumber > currentGames.size()) {
                throw new ResponseException(400, "Expected: valid <ID>");
            }

            int gameID = currentGames.get(gameNumber-1).gameID();

            JoinRequest request = new JoinRequest(authToken,color,gameID);
            JoinResult result = serverFacade.join(request);

            String gameName = currentGames.get(gameNumber-1).gameName();

            //does not pass in the board now, but this is easily changed for later
            ChessBoard.drawChessBoard(color);

            return ("Successfully joined " + gameName);
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }

    private String observe(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameNumber = Integer.parseInt(params[0]);

            if (gameNumber > currentGames.size()) {
                throw new ResponseException(400, "Expected: valid <ID>");
            }

            int gameID = currentGames.get(gameNumber-1).gameID();

            String gameName = currentGames.get(gameNumber-1).gameName();

            //does not pass in the board now, but this is easily changed for later
            ChessBoard.drawChessBoard("WHITE");

            return ("Successfully observing " + gameNumber + " " + gameName);
        }
        throw new ResponseException(400, "Expected: <ID> ");
    }

    private String logout(String authToken, String... params) throws ResponseException {
        if (params.length == 0) {
            LogoutRequest request = new LogoutRequest(authToken);
            LogoutResult result = serverFacade.logout(request);

            return ("Successfully logged out.");
        }
        throw new ResponseException(400, "Expected: nothing after \"logout\"");
    }

    private String help(String cmd) throws ResponseException {
        String firstLine = "Menu:";
        if (cmd != "help") {
            firstLine = "\"" + cmd + "\" is an invalid option, try again: ";
        }

        return (firstLine +
                """
                 
                - create <NAME>  --  create a new game
                - list  --  list of all the games currently open
                - join  --  join a game and play
                - observe  <ID>  --  watch a game
                - logout  --  logout of chess
                - help  --  lists options
                - quit  --  quit playing chess
                 """);
    }
}

