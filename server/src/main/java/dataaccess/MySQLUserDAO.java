package dataaccess;

import model.UserData;

public class MySQLUserDAO implements UserDAO{
    public MySQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
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
