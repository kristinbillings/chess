package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;
    void clear() throws DataAccessException;
    AuthData getUserAuthData(String authToken) throws DataAccessException;
    void deleteAuth(AuthData authData) throws DataAccessException;
}
