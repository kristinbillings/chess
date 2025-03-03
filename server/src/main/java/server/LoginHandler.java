package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import requests.RegisterRequest;
import responses.RegisterResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import requests.LoginRequest;
import responses.LoginResponse;

import java.util.Objects;

public class LoginHandler implements Route {
    private UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    };

    public Object handle(Request req, Response res) throws DataAccessException {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);

        try {
            if (request.username() == null | request.password() == null) {
                res.status(400);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", "Error: bad request");
                return new Gson().toJson(errorResponse);
            }
            LoginResponse response = userService.login(request);
            res.status(200);
            return new Gson().toJson(response);
        }
        catch(DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("401", e.getMessage());
                return new Gson().toJson(errorResponse);
            } else {
                res.status(500);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("500", e.getMessage());
                return new Gson().toJson(errorResponse);
            }
        }
    };
}