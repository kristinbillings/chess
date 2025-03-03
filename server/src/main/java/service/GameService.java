package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import requests.CreateRequest;
import requests.JoinRequest;
import requests.ListRequest;
import results.CreateResult;
import results.ListResult;
import results.JoinResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

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

        //if (allGames.isEmpty()) {
          //  throw new DataAccessException("Error: no current games");
        //}

        //Collection<> listAllGames = new Object[allGames.size()][4];

        int i = 0;
        //for (Integer gameID : allGames.keySet()) {
          //  listAllGames.add(allGames.get(gameID));
        //}

        //ListResult result = new ListResult(listAllGames);
        return null;
    }

    public JoinResult join(JoinRequest request) throws DataAccessException {
        AuthData authData = authDAO.getUserAuthData(request.authToken());
        if (authData == null){
            throw new DataAccessException("Error: unauthorized");
        }

        GameData gameInfo = gameDAO.getGame(request.gameID());
        if (gameInfo == null) {
            throw new DataAccessException("Error: bad request");
        }

        String username = authDAO.getUserAuthData(request.authToken()).username();

        if (Objects.equals(request.playerColor(), "WHITE") && gameInfo.whiteUsername() == null) {
            gameDAO.updateGame(request.gameID(), username,request.playerColor());
        } else if (Objects.equals(request.playerColor(), "BLACK") && gameInfo.blackUsername() == null) {
            gameDAO.updateGame(request.gameID(), username,request.playerColor());
        } else {
            throw new DataAccessException("Error: already taken");
        }

        JoinResult result = new JoinResult("OK");
        return result;
    }
}
