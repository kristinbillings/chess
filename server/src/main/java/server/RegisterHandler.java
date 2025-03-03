package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import service.UserService;
import spark.*;
import requests.RegisterRequest;
import responses.RegisterResponse;

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
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", "Error: bad request");
                return new Gson().toJson(errorResponse);
            }
            RegisterResponse response = userService.register(request);
            res.status(200);
            return new Gson().toJson(response);
        }
        catch(DataAccessException e) {
            res.status(403);
            ErrorStatusMessage errorResponse = new ErrorStatusMessage("403", e.getMessage());
            return new Gson().toJson(errorResponse);

        }

    }
}
