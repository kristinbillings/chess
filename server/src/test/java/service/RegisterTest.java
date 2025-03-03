package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.RegisterResult;

public class RegisterTest {
    private UserService userService;
    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private RegisterResult expected;

    @BeforeEach
    public void setUp(){
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userService = new UserService(authDAO,userDAO);
        expected = new RegisterResult("","");
    }

    @Test
    public void testValidRegistration() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("Steve","urmom","hottie@gmail.com");
        RegisterResult actual = userService.register(request);

        String username = "Steve";
        String authToken = actual.authToken();
        expected = new RegisterResult(username,authToken);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void testInvalidRegistration() throws DataAccessException {
        RegisterRequest request = new RegisterRequest(null,"urmom","hottie@gmail.com");

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(request);
        }, "Expected DataAccessException to be thrown for null username");
    }
}
