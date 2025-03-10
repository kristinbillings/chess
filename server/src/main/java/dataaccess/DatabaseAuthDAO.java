package dataaccess;

import model.AuthData;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseAuthDAO implements AuthDAO {
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
