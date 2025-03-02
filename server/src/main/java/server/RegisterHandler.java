package server;
import com.google.gson.Gson;
import model.UserData;
import service.UserService;
import spark.*;

public class RegisterHandler implements Route {
    public Object handle(Request request, Response response) throws Exception {
        var user = new Gson().fromJson(request.body(), UserData.class);
        user = UserService.register(user);
        return new Gson().toJson(user);;
    }
}
