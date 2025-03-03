package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class LoginTests {
    private UserService userService;
    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private LoginResult expected;

    @BeforeEach
    public void setUp(){
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userService = new UserService(authDAO,userDAO);
        expected = new LoginResult("","");
    }

    @BeforeEach
    public void createUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("Steve","urmom","hottie@gmail.com");
        RegisterResult actual = userService.register(request);
    }

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
        LoginResult actual = userService.login(request);


        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(request);
        }, "Error: unauthorized");
    }
}
