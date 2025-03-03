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
        allAuthData.put(authData.authToken(),authData);
    }

    @Override
    public void clear () {
        if (!allAuthData.isEmpty()){
            allAuthData.clear();
        }
    }

    @Override
    public AuthData getUserAuthData(String authToken) {
        return allAuthData.get(authToken);
    }

    @Override
    public void deleteAuth(AuthData authData){
        allAuthData.remove(authData.authToken());
    }
}
