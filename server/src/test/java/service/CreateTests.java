package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import requests.CreateRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import results.CreateResult;

public class CreateTests {
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
        //expected = new LoginResult("","");
    }

    @BeforeEach
    public void createUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("Steve", "urmom", "hottie@gmail.com");
        RegisterResult register = userService.register(request);
    }

    @Test
    public void testValidCreateGame() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve", "urmom");
        LoginResult actual1 = userService.login(request1);
        CreateRequest request = new CreateRequest("THe WINNERS", actual1.authToken());
        CreateResult actual = gameService.create(request);

        CreateResult expected = new CreateResult(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidAuthTokenCreate() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve","urmom");
        LoginResult actual1 = userService.login(request1);
        CreateRequest request = new CreateRequest("THe WINNERS", "23nlkjdkljadf");

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.create(request);
        }, "Error: unauthorized");
    }

    @Test
    public void testInvalidNameCreate() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve","urmom");
        LoginResult actual1 = userService.login(request1);
        CreateRequest request = new CreateRequest(null, actual1.authToken());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.create(request);
        }, "Error: bad request");
    }

}
