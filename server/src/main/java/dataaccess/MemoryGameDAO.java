package dataaccess;

import model.GameData;
import model.UserData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    // create storage for all the data
    private Map<Integer, GameData> allGameData;
    private int numGames;

    public MemoryGameDAO() {
        this.allGameData = new HashMap<>();
        this.numGames = 1;
    }

    @Override
    public int getGameID() {
        numGames = numGames + 1;
        return numGames - 1;
    }

    @Override
    public void createGame(GameData gameData){
        allGameData.put(gameData.gameID(),gameData);
    }

    @Override
    public Map<Integer, GameData> getAllGameData() {
        return allGameData;
    }
    @Override
    public GameData getGame(Integer gameID) {
        return allGameData.get(gameID);
    }

    @Override
    public void updateGame(Integer gameID, String username, String color) {
        GameData gameData = allGameData.get(gameID);
        //GameData updatedData = gameData;

        if(Objects.equals(color, "WHITE")) {
            GameData updatedData = new GameData(
                    gameID,
                    username,gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game() );
            allGameData.put(gameID, updatedData);

        } else if (Objects.equals(color, "BLACK")) {
            GameData updatedData = new GameData(
                    gameID,
                    gameData.whiteUsername(),
                    username,
                    gameData.gameName(),
                    gameData.game() );

            allGameData.put(gameID, updatedData);
        }
    }

    @Override
    public void clear() {
        allGameData.clear();
    }

}