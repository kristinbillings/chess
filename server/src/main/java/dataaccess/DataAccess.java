package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

public class DataAccess {
    UserData register(UserData data) throws DataAccessException;

    String getUser(String username) throws DataAccessException;
}

