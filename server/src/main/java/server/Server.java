package server;

import dataaccess.*;
import org.eclipse.jetty.util.log.Log;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private UserService userService;
    private GameService gameService;

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        boolean useMemory = false;
        if (useMemory) {
            MemoryUserDAO userDAO = new MemoryUserDAO();
            MemoryAuthDAO authDAO = new MemoryAuthDAO();
            MemoryGameDAO gameDAO = new MemoryGameDAO();
            //this.userService = new UserService(authDAO, userDAO);
            //this.gameService = new GameService(authDAO, gameDAO);
        }

        MySQLUserDAO userDAO = new MySQLUserDAO();
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(authDAO, gameDAO);


        RegisterHandler registerHandler = new RegisterHandler(userService);
        LoginHandler loginHandler = new LoginHandler(userService);
        LogoutHandler logoutHandler = new LogoutHandler(userService);
        CreateHandler createHandler = new CreateHandler(gameService);
        ListHandler listHandler = new ListHandler(gameService);
        JoinHandler joinHandler = new JoinHandler(gameService);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler);
        Spark.post("/session", loginHandler);
        Spark.delete("/session", logoutHandler);
        Spark.post("/game", createHandler);
        Spark.get("/game", listHandler);
        Spark.put("/game", joinHandler);

        Spark.delete("/db",(req, res) -> {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            res.status(200);
            return "{}";
        });

        Spark.exception(Exception.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(Exception ex, Request req, Response res) {
        res.status(500);
        res.body(STR."{\"message\": \"Error:\{ex.getMessage()}\"} ");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
