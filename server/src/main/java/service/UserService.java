package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.UserData;
import model.AuthData;
import requests.*;
import responses.*;

import java.util.Objects;
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
            throw new DataAccessException("Error: already taken");
        }

        UserData userData= new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(userData);

        AuthData authData = new AuthData(generateToken(), request.username());
        authDAO.createAuth(authData);

        RegisterResponse response = new RegisterResponse(userData.username(),authData.authToken());

        return response;
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException {
        UserData userData = userDAO.getUserData(request.username());

        if (userData == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if (!Objects.equals(userData.password(), request.password())) {
            throw new DataAccessException("Error: unauthorized");
        }

        AuthData authData = new AuthData(generateToken(), request.username());
        authDAO.createAuth(authData);

        LoginResponse response = new LoginResponse(userData.username(),authData.authToken());

        return response;
    }

    public LogoutResponse logout(LogoutRequest request) throws DataAccessException {
        AuthData authData = authDAO.getUserAuthData(request.authToken());

        if (authData == null){
            throw new DataAccessException("Error: Unauthorized");
        }
        authDAO.deleteAuth(authData);

        LogoutResponse response = new LogoutResponse("OK");
        return response;
    }
    //public void logout(UserData user) {}

}
