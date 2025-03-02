package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import service.UserService;
import spark.*;

public class Server {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private UserService userService;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        userService = new UserService();
        RegisterHandler registerHandler = new RegisterHandler(userService);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler);

        Spark.delete("/db",(req, res) -> {
            userDAO.clear();
            res.status(200);
            return "{}";
        });

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
