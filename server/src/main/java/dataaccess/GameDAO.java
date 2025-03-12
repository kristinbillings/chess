package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Map;

public interface GameDAO {
    void createGame(GameData gameData) throws ResponseException;

    int getGameID() throws ResponseException;

    Map<Integer, GameData> getAllGameData() throws ResponseException;

    GameData getGame(Integer gameID) throws ResponseException;

    void updateGame(Integer gameID, String username, String color) throws ResponseException;

    void clear() throws ResponseException;
}
