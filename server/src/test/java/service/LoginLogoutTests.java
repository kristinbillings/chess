package service;

import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import results.LogoutResult;

public class LoginLogoutTests {
    private UserService userService;
    private MySQLAuthDAO authDAO; //changed from memory to database
    private MySQLUserDAO userDAO; //changed from memory to database
    private LoginResult expected;

    @BeforeEach
    public void setUp(){
        authDAO = new MySQLAuthDAO(); //changed from memory to database
        userDAO = new MySQLUserDAO(); //changed from memory to database
        userService = new UserService(authDAO,userDAO);
        //expected = new LoginResult("","");
    }

    @BeforeEach
    public void createUser() throws DataAccessException,ResponseException  {
        RegisterRequest request = new RegisterRequest("Steve","urmom","hottie@gmail.com");
        RegisterResult register = userService.register(request);
    }

    @BeforeEach @AfterEach
    public void clearDatabase() throws DataAccessException, ResponseException {
        authDAO.clear();
        userDAO.clear();
    }

    //LOGIN TESTS
    @Test
    public void testValidRLogin() throws DataAccessException, ResponseException {
        LoginRequest request = new LoginRequest("Steve","urmom");
        LoginResult actual = userService.login(request);

        String username = "Steve";
        String authToken = actual.authToken();
        expected = new LoginResult(username,authToken);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidLogin() throws DataAccessException {
        LoginRequest request = new LoginRequest("Steve","urmo");

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(request);
        }, "Error: unauthorized");
    }

    //LOGOUT TESTS
    @Test
    public void testValidLogout() throws DataAccessException,ResponseException  {
        LoginRequest request1 = new LoginRequest("Steve","urmom");
        LoginResult actual1 = userService.login(request1);
        LogoutRequest request = new LogoutRequest(actual1.authToken());
        LogoutResult actual = userService.logout(request);

        LogoutResult expected = new LogoutResult("OK");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidAuthTokenLogout() throws DataAccessException,ResponseException  {
        LoginRequest request1 = new LoginRequest("Steve","urmom");
        LoginResult actual1 = userService.login(request1);
        LogoutRequest request = new LogoutRequest("23nlkjdkljadf");

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(request);
        }, "Error: unauthorized");
    }
}
