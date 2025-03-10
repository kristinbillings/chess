package dataaccess;

import model.UserData;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseUserDAO implements UserDAO{
    public DatabaseUserDAO() throws DataAccessException {
        configureDatabase();
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
