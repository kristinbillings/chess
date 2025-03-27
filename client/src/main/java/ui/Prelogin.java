package ui;

import dataaccess.UserDAO;
import exceptions.ResponseException;
import model.UserData;
import net.ServerFacade;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Prelogin {
    private final String serverUrl;
    private ServerFacade serverFacade;
    private String authToken;
    private List<String> existingRegisters;

    public Prelogin(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.existingRegisters = new ArrayList<>();
    }

    public String getAuth() {
        return authToken;
    }

    public String evaluate(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
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

            //if(!existingRegisters.contains(username)) {
              //  return "This user does not exist, try again.\n";
            //}



            LoginRequest request = new LoginRequest(username, password);
            try {
                LoginResult result = serverFacade.login(request);
                authToken = result.authToken();
            }catch(ResponseException e) {
                throw new ResponseException(400, "Invalid username or password\n");
            }
            return "Successful login!\n";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>\n");
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];

            //if (existingRegisters.contains(username)) {
              //  return "Username already used, choose new one.\n";
            //} else {
             //   existingRegisters.add(username);
            //}

            RegisterRequest request = new RegisterRequest(username, password, email);
            try {
                RegisterResult result = serverFacade.register(request);
                authToken = result.authToken();
            } catch(ResponseException e) {
                throw new ResponseException(400, "Username already being used, try another one.\n");
            }

            return "User successfully registered!\n";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>\n");
    }

    private String help(String cmd) throws ResponseException {
        String firstLine = "\nMenu:";
        if (cmd == "help\n") {
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
