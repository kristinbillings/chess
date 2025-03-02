package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private Map<String, AuthData> allAuthData;

    public MemoryAuthDAO() {
        this.allAuthData = new HashMap<>();
    }

    @Override
    public void createAuth(AuthData authData) {
        allAuthData.put(authData.username(),authData);
    }

    @Override
    public void clear () {
        if (!allAuthData.isEmpty()){
            allAuthData.clear();
        }
    }
}
