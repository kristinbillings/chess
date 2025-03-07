package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void clear();
    AuthData getUserAuthData(String authToken);
    void deleteAuth(AuthData authData);
}
