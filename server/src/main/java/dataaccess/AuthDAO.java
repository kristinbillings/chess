package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void clear();
}
