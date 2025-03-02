package service;

import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import dataaccess.DataAccess;

public class UserService {

    public AuthData register(UserData user) throws DataAccessException {
        // creat user and creat auth token then return
        if (DataAccess.getUser(user.username()) != null){
            throw new DataAccessException("Username already used");
        }
        return DataAccess.register(user);;
    }

    //public AuthData login(UserData user) {}

    //public void logout(UserData user) {}

}
