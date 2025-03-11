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
import results.GameResult;
import results.ListResult;
import results.JoinResult;

import java.util.*;

public class GameService {
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public GameService(MemoryAuthDAO authDAO, MemoryUserDAO userDAO, MemoryGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateResult create(CreateRequest request) throws DataAccessException {
        AuthData authData = authDAO.getUserAuthData(request.authToken());

        if (authData == null){
            throw new DataAccessException("Error: unauthorized");
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

        List<GameResult> listAllGames = new ArrayList<>();

        //Map<Object,Object>

        //int i = 0;
        for (Integer gameID : allGames.keySet()) {
            GameResult game = new GameResult(
                    allGames.get(gameID).gameID(),
                    allGames.get(gameID).whiteUsername(),
                    allGames.get(gameID).blackUsername(),
                    allGames.get(gameID).gameName());

            listAllGames.add(game);
        }

        return new ListResult(listAllGames);
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

        JoinResult result = new JoinResult("");
        return result;
    }
}
