package server;
import com.google.gson.Gson;
import model.UserData;
import service.UserService;
import spark.*;
import requests.*;
import responses.*;

public class RegisterHandler implements Route {
    public Object handle(Request req, Response res) throws Exception {

        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
        UserService service = new UserService();
        RegisterResponse response = service.register(request);
        return new Gson().toJson(response);
    }
}
