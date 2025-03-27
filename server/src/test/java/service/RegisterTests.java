package service;

import dataaccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.RegisterResult;
import exceptions.ResponseException;

public class RegisterTests {
    private UserService userService;
    private MySQLAuthDAO authDAO; //changed from memory to database
    private MySQLUserDAO userDAO; //changed from memory to database
    private RegisterResult expected;

    @BeforeEach
    public void setUp(){
        authDAO = new MySQLAuthDAO(); //changed from memory to database
        userDAO = new MySQLUserDAO(); //changed from memory to database
        userService = new UserService(authDAO,userDAO);
        //expected = new RegisterResult("","");
    }

    @BeforeEach @AfterEach
    public void clearDatabase() throws DataAccessException, ResponseException {
        authDAO.clear();
        userDAO.clear();
    }

    @Test
    public void testValidRegistration() throws DataAccessException,ResponseException {
        RegisterRequest request = new RegisterRequest("Steve","urmom","hottie@gmail.com");
        RegisterResult actual = userService.register(request);

        String username = "Steve";
        String authToken = actual.authToken();
        expected = new RegisterResult(username,authToken);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidRegistration() throws DataAccessException,ResponseException {
        RegisterRequest request = new RegisterRequest(null,"urmom","hottie@gmail.com");

        Assertions.assertThrows(dataaccess.ResponseException.class, () -> {
            userService.register(request);
        }, "Error: unable to update user");
    }
}
