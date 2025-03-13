package dataaccess;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

public class RegisterDatabaseTests {
    private final String[] createStatement1 = {
            """
            CREATE TABLE IF NOT EXISTS  GameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private final String[] createStatement2 = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private final String[] createStatement3 = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` varchar(256) NOT NULL  ,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @BeforeEach
    public void setUp(){
        try {
            DatabaseManager.createDatabase();
            ConfigureDatabase.configureDatabase(createStatement1);
            ConfigureDatabase.configureDatabase(createStatement2);
            ConfigureDatabase.configureDatabase(createStatement3);
        } catch (ResponseException e) {
            System.err.println("Error configuring the database: " + e.getMessage());
            throw new RuntimeException("Database configuration failed", e);  // You can customize this if you need to propagate it
        }
    }

    @BeforeEach @AfterEach
    public void clearDatabase() throws DataAccessException, ResponseException {
        var statement1 = "TRUNCATE GameData";
        ConfigureDatabase.executeUpdate(statement1);
        var statement2 = "TRUNCATE AuthData";
        ConfigureDatabase.executeUpdate(statement2);
        var statement3 = "TRUNCATE UserData";
        ConfigureDatabase.executeUpdate(statement3);
    }

    @Test
    public void testCreateAuth() throws ResponseException {
        UserData userData = new UserData("john", "password",".com");
        MySQLUserDAO.createUser(userData)

    }



}
