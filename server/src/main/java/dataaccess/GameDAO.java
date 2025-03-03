package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Map;

public interface GameDAO {
    void createGame(GameData gameData);

    int getGameID();

    Map<Integer, GameData> getAllGameData();

    GameData getGame(Integer gameID);

    void updateGame(Integer gameID, String username, String color);
}
