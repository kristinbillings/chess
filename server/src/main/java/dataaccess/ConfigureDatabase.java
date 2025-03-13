package dataaccess;

import java.sql.SQLException;

public class ConfigureDatabase {
    public static void configureDatabase(String[] createStatements) {
        //DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | ResponseException ex) {
            throw new RuntimeException("Unable to configure database: %s");
        }
    }
}
