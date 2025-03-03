package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
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
        AuthData authData = authDAO.getUserAuthData(request.authToken());

        if (request.gameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        //if (authData == null){
          //  throw new DataAccessException("Error: Unauthorized");
        //}

        int gameID = gameDAO.getGameID();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID,"","", request.gameName(), game);
        gameDAO.createGame(gameData);

        CreateResult result = new CreateResult(gameData.gameID());
        return result;
    }
}
