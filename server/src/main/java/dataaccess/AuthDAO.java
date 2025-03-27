package dataaccess;

import model.AuthData;
import exceptions.ResponseException;


public interface AuthDAO {
    void createAuth(AuthData authData) throws ResponseException;
    void clear() throws DataAccessException, ResponseException;
    AuthData getUserAuthData(String authToken) throws ResponseException;
    void deleteAuth(AuthData authData) throws ResponseException;
}
