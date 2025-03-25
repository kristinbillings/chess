package ui;

import dataaccess.ResponseException;
import net.ServerFacade;
import requests.*;
import results.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Postlogin {
    private final String serverUrl;
    private ServerFacade serverFacade;
    private List<GameResult> currentGames;

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
                case "observe" -> observe();
                case "logout" -> logout();
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
            var gameNumber = params[0];
            var color = params[1];

            if (!Objects.equals(color, "WHITE") && !Objects.equals(color, "BLACK")) {
                throw new ResponseException(400, "Expected: [WHITE|BLACK]");
            }



            JoinRequest request = new JoinRequest(authToken,color,);
            JoinResult result = serverFacade.join(request);

            return ("Successfully created a game called: " + gameName);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    private String observe(String... params) throws ResponseException {
        return null;
    }
    private String logout(String... params) throws ResponseException {
        return null;
    }
    private String help(String cmd) throws ResponseException {
        return null;
    }
}

