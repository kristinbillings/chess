package dataaccess;

import model.AuthData;
import exceptions.ResponseException;

public class MySQLAuthDAO implements AuthDAO {
    public MySQLAuthDAO() {
        try {
            DatabaseManager.createDatabase();
            ConfigureDatabase.configureDatabase(createStatements);
        } catch (ResponseException e) {
            System.err.println("Error configuring the database: " + e.getMessage());
            throw new RuntimeException("Database configuration failed", e);  // You can customize this if you need to propagate it
        }    }

    @Override
    public void createAuth(AuthData authData) throws ResponseException {
        var statement = "INSERT INTO AuthData (username, authToken) VALUES (?, ?)";
        ConfigureDatabase.executeUpdate(statement, authData.username(), authData.authToken());
    }

    @Override
    public void clear () throws ResponseException {
        var statement = "TRUNCATE AuthData";
        ConfigureDatabase.executeUpdate(statement);
    }

    @Override
    public AuthData getUserAuthData(String authToken) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM AuthData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(authToken, rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws ResponseException{
        var statement = "DELETE FROM AuthData WHERE authToken=?";
        ConfigureDatabase.executeUpdate(statement, authData.authToken());
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
