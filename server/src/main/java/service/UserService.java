package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.UserData;
import model.AuthData;
import requests.*;
import results.*;

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

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        if (userDAO.getUserData(request.username()) != null){
            throw new DataAccessException("Error: already taken");
        }

        UserData userData= new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(userData);

        AuthData authData = new AuthData(generateToken(), request.username());
        authDAO.createAuth(authData);

        RegisterResult result = new RegisterResult(userData.username(),authData.authToken());

        return result;
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        UserData userData = userDAO.getUserData(request.username());

        if (userData == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if (!Objects.equals(userData.password(), request.password())) {
            throw new DataAccessException("Error: unauthorized");
        }

        AuthData authData = new AuthData(generateToken(), request.username());
        authDAO.createAuth(authData);

        LoginResult result = new LoginResult(userData.username(),authData.authToken());

        return result;
    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException {
        AuthData authData = authDAO.getUserAuthData(request.authToken());

        if (authData == null){
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.deleteAuth(authData);

        LogoutResult result = new LogoutResult("OK");
        return result;
    }
}
