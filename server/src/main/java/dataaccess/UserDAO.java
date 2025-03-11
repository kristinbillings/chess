package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUserData(String username) throws ResponseException;

    void createUser(UserData userData) throws ResponseException;

    void clear() throws ResponseException;
}
