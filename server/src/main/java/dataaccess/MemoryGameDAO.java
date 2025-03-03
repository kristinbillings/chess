package dataaccess;

import model.GameData;
import model.UserData;
import java.util.HashMap;
import java.util.Map;

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
}