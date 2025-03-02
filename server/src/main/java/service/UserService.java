package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.UserData;
import model.AuthData;
import requests.RegisterRequest;
import responses.RegisterResponse;
import java.util.UUID;

public class UserService {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException {

        if (userDAO.getUserData(request.username()) != null){
            throw new DataAccessException("Username already used");
        }

        UserData userData= new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(userData);

        AuthData authData = new AuthData(generateToken(), request.username());
        authDAO.createAuth(authData);

        return new RegisterResponse(userData.username(),authData.authToken());
    }


    //public AuthData login(UserData user) {}

    //public void logout(UserData user) {}

}
