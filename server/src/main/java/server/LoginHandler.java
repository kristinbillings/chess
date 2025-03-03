package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.RegisterRequest;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    private UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    };

    public Object handle(Request req, Response res) throws DataAccessException {
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);

        try {

            LoginResponse response = userService.login(request);

        }
    };
}