package ui;

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
    private String authToken;

    public Postlogin(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluate(String input, String authToken) {
        this.authToken = authToken;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help(cmd);
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String create(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameName = params[0];
            CreateRequest request = new CreateRequest(gameName,authToken);
            CreateResult result = serverFacade.create(request);

            //System.out.println("params[0]: " + params[0]);

            return ("Successfully created a game called: " + gameName);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    private String list(String... params) throws ResponseException {
        if (params.length == 0) {
            ListRequest request = new ListRequest(authToken);
            ListResult result = serverFacade.list(request);

            String output = "\nCurrent Games: \n";
            currentGames.clear(); //resets so number are consistent

            for (int i = 0; i < result.games().size(); i++) {
                GameResult game = result.games().get(i);
                currentGames.add(game);
                String num = Integer.toString(i+1);

                String whitePlayer = "";
                if (game.whiteUsername() == null){
                    whitePlayer = "<player space available>";
                } else {
                    whitePlayer = game.whiteUsername();
                }

                String blackPlayer = "";
                if (game.blackUsername() == null){
                    blackPlayer = "<player space available>";
                } else {
                    blackPlayer = game.blackUsername();
                }

                output += num///changed here
                        + "\t" + game.gameName()
                        + ", White: " + whitePlayer
                        + ", Black: " + blackPlayer
                        + "\n";
            }
            output += "---------------------------------------------------\n";
            return output;
        }
        throw new ResponseException(400, "Expected: nothing after \"list\"");
    }

    private String join(String... params) throws ResponseException {
        if (params.length == 2) {
            var color = params[1];
            int gameNumber = 0;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected: a valid integer\n");
            }

            if (!Objects.equals(color, "white")) {
                if (!Objects.equals(color, "black")) {
                    throw new ResponseException(400, "Expected: [WHITE|BLACK]\n");
                }
            }
            if (gameNumber > currentGames.size()) {
                throw new ResponseException(400, "Expected: valid <ID>\n");
            }

            int gameID = currentGames.get(gameNumber-1).gameID();

            JoinRequest request = new JoinRequest(authToken,color,gameID);
            JoinResult result = serverFacade.join(request);

            String gameName = currentGames.get(gameNumber-1).gameName();

            //does not pass in the board now, but this is easily changed for later
            ChessBoard.drawChessBoard(color);

            return ("Successfully joined " + gameName);
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]\n");
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

    private String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            LogoutRequest request = new LogoutRequest(authToken);
            LogoutResult result = serverFacade.logout(request);

            return ("Successfully logged out.");
        }
        throw new ResponseException(400, "Expected: nothing after \"logout\"");
    }

    private String help(String cmd) throws ResponseException {
        String firstLine = "\nMenu:";
        if (cmd == "help\n") {
            firstLine = "\"" + cmd + "\" is an invalid option, try again: ";
        }

        return (firstLine +
                """
                 
                - create <NAME>  --  create a new game
                - list  --  list of all the games currently open
                - join <ID> [WHITE|BLACK]  --  join a game and play
                - observe  <ID>  --  watch a game
                - logout  --  logout of chess
                - help  --  lists options
                - quit  --  quit playing chess
                 """);
    }
}

