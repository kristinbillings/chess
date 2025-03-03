package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import requests.ListRequest;
import results.CreateResult;
import results.LoginResult;
import results.RegisterResult;
import results.ListResult;
import results.GameResult;

import java.util.ArrayList;
import java.util.Arrays;


public class ListTests {
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
    public void testValidList2Games() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve", "urmom");
        LoginResult actual1 = userService.login(request1);
        CreateRequest request2 = new CreateRequest("THe WINNERS", actual1.authToken());
        CreateResult actual2 = gameService.create(request2);
        CreateRequest request3 = new CreateRequest("homies", actual1.authToken());
        CreateResult actual3 = gameService.create(request3);

        ListRequest request = new ListRequest(actual1.authToken());
        ListResult actual = gameService.listGames(request);

        GameResult game1 = new GameResult(
                actual2.gameID(),
                null,
                null,
                "THe WINNERS");

        GameResult game2 = new GameResult(
                actual3.gameID(),
                null,
                null,
                "homies");

        ListResult expected = new ListResult(new ArrayList<>(Arrays.asList(game1, game2)));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testValidListNOGames() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve", "urmom");
        LoginResult actual1 = userService.login(request1);

        ListRequest request = new ListRequest(actual1.authToken());
        ListResult actual = gameService.listGames(request);

        ListResult expected = new ListResult(new ArrayList<>());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidAuthTokenList() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve","urmom");
        LoginResult actual1 = userService.login(request1);
        ListRequest request = new ListRequest("actual1.authToken() lololo");

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames(request);
        }, "Error: unauthorized");
    }
}
