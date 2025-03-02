package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import responses.RegisterResponse;

public class RegisterTest {
    private UserService userService;
    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private RegisterResponse expected;

    @BeforeEach
    public void setUp(){
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userService = new UserService(authDAO,userDAO);
        expected = new RegisterResponse("","");

    }

    @Test
    public void testValidRegistration() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("Steve","urmom","hottie@gmail.com");
        RegisterResponse actual = userService.register(request);

        String username = "Steve";
        String authToken = actual.authToken();
        expected = new RegisterResponse(username,authToken);

        Assertions.assertEquals(expected, actual);

    }
}
