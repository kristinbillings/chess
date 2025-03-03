package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import requests.LogoutRequest;
import responses.LogoutResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.Reader;
import java.util.Set;

public class LogoutHandler implements Route {
    private UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    ;

    public Object handle(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        LogoutRequest request = new LogoutRequest(authToken);

        try {
            if (request.authToken() == null) {
                res.status(500);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("500", "Error: no authToken");
                return new Gson().toJson(errorResponse);
            }
            LogoutResponse response = userService.logout(request);
            res.status(200);
            ErrorStatusMessage finalResponse = new ErrorStatusMessage("500", "OK");
            return new Gson().toJson(finalResponse);
        }
        catch( DataAccessException e) {
            res.status(500);
            ErrorStatusMessage errorResponse = new ErrorStatusMessage("500", e.getMessage());
            return new Gson().toJson(errorResponse);
        }
    }
}