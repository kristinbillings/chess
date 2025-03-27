package dataaccess;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import exceptions.ResponseException;


import service.UserService;

public class RegisterDatabaseTests {
    private MySQLUserDAO userDAO = new MySQLUserDAO();

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
        this.userDAO = new MySQLUserDAO();

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
    public void testCreateUser() throws ResponseException, SQLException {
        UserData userData = new UserData("john", "password",".com");
        userDAO.createUser(userData);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT username, password, email FROM UserData WHERE username=?")) {
            ps.setString(1, "john");
            try (ResultSet rs = ps.executeQuery()) {
                Assertions.assertTrue(rs.next(), "User should exist in the database");
                Assertions.assertEquals("john", rs.getString("username"));
                Assertions.assertEquals(".com", rs.getString("email"));
            }
        }
    }

    @Test
    public void testNegCreateUser() throws ResponseException, SQLException {
        UserData userData = new UserData(null, "password",".com");
        Assertions.assertThrows(ResponseException.class, () -> {
            userDAO.createUser(userData);
        });
    }

    @Test
    public void testGetUser() throws ResponseException, SQLException {
        UserData userData = new UserData("sue", "ps",".gm");
        userDAO.createUser(userData);

        UserData newData = userDAO.getUserData("sue");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT username, password, email FROM UserData WHERE username=?")) {
            ps.setString(1, "sue");
            try (ResultSet rs = ps.executeQuery()) {
                Assertions.assertTrue(rs.next(), "User should exist in the database");
                Assertions.assertEquals("sue", rs.getString("username"));
                Assertions.assertEquals(".gm", rs.getString("email"));
            }
        }
    }

    @Test
    public void testNegGetUser() throws ResponseException, SQLException {
        UserData userData = new UserData("sue", "password",".com");
        Assertions.assertThrows(ResponseException.class, () -> {
            UserData newData = userDAO.getUserData(null);
        });
    }

    @Test
    public void testCheckPassword() throws ResponseException, SQLException {
        UserData userData = new UserData("sue", "ps",".gm");
        userDAO.createUser(userData);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT username, password, email FROM UserData WHERE username=?")) {
            ps.setString(1, "sue");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Step 3: Check if the plain password matches the hashed password
                    Assertions.assertTrue(BCrypt.checkpw("ps", rs.getString("password")),
                            "Password should match the hashed password in the database");
                }
            }
        }
    }

    @Test
    public void testNegCheckPassword() throws ResponseException, SQLException {
        UserData userData = new UserData("sue", "ps",".gm");
        userDAO.createUser(userData);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT username, password, email FROM UserData WHERE username=?")) {
            ps.setString(1, "sue");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Step 3: Check if the plain password matches the hashed password
                    Assertions.assertFalse(BCrypt.checkpw("ls", rs.getString("password")),
                            "Password should match the hashed password in the database");
                }
            }
        }
    }


    @Test
    public void testClearUser() throws ResponseException, SQLException {
        UserData userData = new UserData("sue", "ps",".gm");
        userDAO.createUser(userData);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("TRUNCATE UserData")) {
             ps.executeUpdate();
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT username, password, email FROM UserData WHERE username=?")) {
            ps.setString(1, "sue");
            try (ResultSet rs = ps.executeQuery()) {
                Assertions.assertFalse(rs.next(), "User should be removed from database after TRUNCATE");
            }
        }
    }

}
