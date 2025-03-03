package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    private UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    ;

    public Object handle(Request req, Response res) throws DataAccessException {
        LogoutRequest request = new Gson().fromJson(req.body(), LogoutRequest.class);

        try {

        }
    }
}