package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;
import requests.CreateRequest;
import results.CreateResult;
import results.RegisterResult;

public class GameService {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public GameService(MemoryAuthDAO authDAO, MemoryUserDAO userDAO, MemoryGameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateResult create(CreateRequest request) throws DataAccessException {
        GameData gameData = gameDAO;


        CreateResult result = new CreateResult(userData.username(),authData.authToken());
        return result;
    }
}
