package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
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
    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private LoginResult expected;

    @BeforeEach
    public void setUp(){
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userService = new UserService(authDAO,userDAO);
        //expected = new LoginResult("","");
    }

    @BeforeEach
    public void createUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("Steve","urmom","hottie@gmail.com");
        RegisterResult register = userService.register(request);
    }

    //LOGIN TESTS
    @Test
    public void testValidRLogin() throws DataAccessException {
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
    public void testValidLogout() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve","urmom");
        LoginResult actual1 = userService.login(request1);
        LogoutRequest request = new LogoutRequest(actual1.authToken());
        LogoutResult actual = userService.logout(request);

        LogoutResult expected = new LogoutResult("OK");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidAuthTokenLogout() throws DataAccessException {
        LoginRequest request1 = new LoginRequest("Steve","urmom");
        LoginResult actual1 = userService.login(request1);
        LogoutRequest request = new LogoutRequest("23nlkjdkljadf");

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(request);
        }, "Error: unauthorized");
    }
}
