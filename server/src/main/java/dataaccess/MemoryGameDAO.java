package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements UserDAO {
    // create storage for all the data
    private Map<String, UserData> allGameData;

    public MemoryUserDAO() {
        this.allGameData = new HashMap<>();
    }

    @Override
    public GameData createGame(){

    }
}