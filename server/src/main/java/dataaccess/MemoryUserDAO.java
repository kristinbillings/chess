package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    // create storage for all the data
    private Map<String, UserData> allUserData;

    public MemoryUserDAO() {
        this.allUserData = new HashMap<>();
    }

    @Override
    public UserData getUserData(String username) {
        return allUserData.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        allUserData.put(userData.username(), userData);
    }

    @Override
    public void clear() {
        allUserData.clear();

    }
}
