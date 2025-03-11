package dataaccess;

import model.AuthData;


public class MySQLAuthDAO implements AuthDAO {
    public MySQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
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
