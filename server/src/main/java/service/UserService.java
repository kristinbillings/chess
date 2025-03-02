package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.UserData;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;
import requests.RegisterRequest;
import responses.RegisterResponse;
import java.util.UUID;

public class UserService {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    public UserService(MemoryAuthDAO authDAO, MemoryUserDAO userDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException {


        if (userDAO.getUserData(request.username()) != null){
            throw new DataAccessException("already taken");
        }

        UserData userData= new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(userData);

        AuthData authData = new AuthData(generateToken(), request.username());
        authDAO.createAuth(authData);

        RegisterResponse response = new RegisterResponse(userData.username(),authData.authToken());

        return response;
    }


    //public AuthData login(UserData user) {}

    //public void logout(UserData user) {}

}
