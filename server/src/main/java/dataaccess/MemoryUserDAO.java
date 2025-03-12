package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public boolean checkPassword(String storedPW, String reqPW) {
        return Objects.equals(storedPW, reqPW);
    }
}
