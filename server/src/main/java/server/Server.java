package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.eclipse.jetty.util.log.Log;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private UserService userService;

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        this.userService = new UserService(authDAO, userDAO);

        RegisterHandler registerHandler = new RegisterHandler(userService);
        LoginHandler loginHandler = new LoginHandler(userService);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler);
        Spark.post("/session", loginHandler);

        Spark.delete("/db",(req, res) -> {
            userDAO.clear();
            authDAO.clear();
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
