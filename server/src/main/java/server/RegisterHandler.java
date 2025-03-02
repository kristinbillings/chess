package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.UserData;
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
        //UserService service = new UserService();
        RegisterResponse response = userService.register(request);
        return new Gson().toJson(response);
    }
}
