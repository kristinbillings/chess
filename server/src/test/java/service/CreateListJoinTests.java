package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.*;
import results.*;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateListJoinTests {
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

    //CREATE TESTS
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

    //LIST TESTS
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

    //JOIN TESTS
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
