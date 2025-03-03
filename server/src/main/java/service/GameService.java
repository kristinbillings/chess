package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import requests.CreateRequest;
import requests.ListRequest;
import results.CreateResult;
import results.ListResult;
import results.RegisterResult;

import java.util.Map;

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

        if (authData == null){
            throw new DataAccessException("Error: Unauthorized");
        }
        if (request.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }

        int gameID = gameDAO.getGameID();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID,null,null, request.gameName(), game);
        gameDAO.createGame(gameData);

        CreateResult result = new CreateResult(gameData.gameID());
        return result;
    }

    public ListResult listGames(ListRequest request) throws DataAccessException {
        AuthData authData = authDAO.getUserAuthData(request.authToken());
        if (authData == null){
            throw new DataAccessException("Error: unauthorized");
        }

        Map<Integer, GameData> allGames = gameDAO.getAllGameData();

        if (allGames == null) {
            throw new DataAccessException("Error: no current games");
        }
        ListResult result = new ListResult(allGames);

        return result;
    }
}
