package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorBadRequest;
import service.UserService;
import spark.*;
import requests.*;
import responses.*;

public class RegisterHandler implements Route {
    private UserService userService;

    public RegisterHandler(UserService userService){
        this.userService = userService;
    };

    public Object handle(Request req, Response res) throws DataAccessException {
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);

        try {
            if (request.username() == null|request.password() == null|request.email() == null) {
                res.status(400);
                ErrorBadRequest errorResponse = new ErrorBadRequest("400", "Error: bad request");
                return new Gson().toJson(errorResponse);
            }
            RegisterResponse response = userService.register(request);
            res.status(200);
            return new Gson().toJson(response);
        }
        catch(DataAccessException e) {
            res.status(403);
            ErrorBadRequest errorResponse = new ErrorBadRequest("403", e.getMessage());
            return new Gson().toJson(errorResponse);

        }

    }
}
