package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import java.sql.*;
import java.util.Properties;


public class MySQLUserDAO implements UserDAO{
    public MySQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUserData(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM username WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readPet(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
        return allUserData.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        var statement = "INSERT INTO Userdata (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(userData);
        var id = executeUpdate(statement, userData.username(), userData.password(),userData.email(), json);
        return new UserData(id, pet.name(), pet.type());
    }

    @Override
    public void clear() {
        allUserData.clear();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` varchar(256) NOT NULL  ,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(username),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
