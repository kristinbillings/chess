package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import dataaccess.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import requests.LoginRequest;
import results.LoginResult;

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
            LoginResult response = userService.login(request);
            //res.status(200);
            return new Gson().toJson(response);
        }
        catch(DataAccessException e) {
            ErrorMessages errorMessage = new ErrorMessages();
            return new Gson().toJson(errorMessage.errorMessages(e,res));
        } catch (ResponseException e) {
            return new Gson().toJson(e.getMessage());
        }
    };
}