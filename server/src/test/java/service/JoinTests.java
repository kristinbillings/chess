package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateRequest;
import requests.ListRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import requests.JoinRequest;
import results.*;

import java.util.ArrayList;
import java.util.Arrays;

public class JoinTests {
    private UserService userService;
    private GameService gameService;
    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private MemoryGameDAO gameDAO;

    @BeforeEach
    public void setUp() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(authDAO, userDAO, gameDAO);
    }

    @BeforeEach
    public void createUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("Steve", "urmom", "hottie@gmail.com");
        RegisterResult register = userService.register(request);
    }

    @Test
    public void testValidJoin() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve", "urmom");
        LoginResult actual1 = userService.login(request1);
        CreateRequest request2 = new CreateRequest("THe WINNERS", actual1.authToken());
        CreateResult actual2 = gameService.create(request2);
        CreateRequest request3 = new CreateRequest("homies", actual1.authToken());
        CreateResult actual3 = gameService.create(request3);

        JoinRequest request = new JoinRequest(actual1.authToken(), "WHITE", actual2.gameID());
        JoinResult actual = gameService.join(request);

        JoinResult expected = new JoinResult("");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidJoinNOGames() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve", "urmom");
        LoginResult actual1 = userService.login(request1);

        JoinRequest request = new JoinRequest(actual1.authToken(), "WHITE", 5);


        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.join(request);
        }, "Error: unauthorized");
    }

    @Test
    public void testInvalidAuthTokenJoin() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve", "urmom");
        LoginResult actual1 = userService.login(request1);
        CreateRequest request2 = new CreateRequest("THe WINNERS", actual1.authToken());
        CreateResult actual2 = gameService.create(request2);
        CreateRequest request3 = new CreateRequest("homies", actual1.authToken());
        CreateResult actual3 = gameService.create(request3);

        JoinRequest request = new JoinRequest("dsfsdfadf444", "WHITE", 1);


        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.join(request);
        }, "Error: unauthorized");
    }

    @Test
    public void testInvalidJoinColorAlreadyTaken() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve", "urmom");
        LoginResult actual1 = userService.login(request1);
        CreateRequest request2 = new CreateRequest("THe WINNERS", actual1.authToken());
        CreateResult actual2 = gameService.create(request2);
        CreateRequest request3 = new CreateRequest("homies", actual1.authToken());
        CreateResult actual3 = gameService.create(request3);

        JoinRequest requestJ = new JoinRequest(actual1.authToken(), "WHITE", 1);
        JoinResult actualJ = gameService.join(requestJ);

        JoinRequest request = new JoinRequest(actual1.authToken(), "WHITE", 1);


        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.join(request);
        }, "Error: already taken");
    }
}
