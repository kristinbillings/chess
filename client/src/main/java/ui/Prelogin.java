package ui;

import dataaccess.ResponseException;
import net.ServerFacade;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

import java.util.Arrays;

public class Prelogin {
    private final String serverUrl;
    private ServerFacade serverFacade;

    public Prelogin(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluate(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "help" -> help(cmd);
                case "quit" -> "quit";
                default -> help(cmd);
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            LoginRequest request = new LoginRequest(username, password);
            LoginResult result = serverFacade.login(request);

            return "Successful login!";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];

            RegisterRequest request = new RegisterRequest(username, password, email);
            RegisterResult result = serverFacade.register(request);

            return "User successfully registered!";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    private String help(String cmd) throws ResponseException {
        String firstLine = "Menu:";
        if (cmd != "help") {
            firstLine = "\"" + cmd + "\" is an invalid option, try again: ";
        }

        return (firstLine +
                """
                                
                - login <USERNAME> <PASSWORD>  --  login to play
                - register <USERNAME> <PASSWORD> <EMAIL>  --  register to begin playing
                - quit  --  exits chess program
                - help  --  lists options
                
                Choose one:
                """);
    }

}
