package ui;

import dataaccess.ResponseException;
import net.ServerFacade;
import requests.CreateRequest;
import requests.LoginRequest;
import results.CreateResult;
import results.LoginResult;

import java.util.Arrays;

public class Postlogin {
    private final String serverUrl;
    private ServerFacade serverFacade;

    public Postlogin(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluate(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe();
                case "logout" -> logout();
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
            CreateRequest request = new CreateRequest(gameName,);
            CreateResult result = serverFacade.create(request);

            return ("Successfully created a game called: " + gameName);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }


    private String list(String... params) throws ResponseException {
        return null;
    }

    private String join(String... params) throws ResponseException {
        return null;
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

