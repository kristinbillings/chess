package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO {
    // create storage for all the data
    private int numGames;

    public MySQLGameDAO() {
        this.numGames = 1;
        try {
            DatabaseManager.createDatabase();
            configureDatabase();
        } catch (ResponseException e) {
            System.err.println("Error configuring the database: " + e.getMessage());
            throw new RuntimeException("Database configuration failed", e);  // You can customize this if you need to propagate it
        }
    }


    @Override
    public int createGame(GameData gameData) throws ResponseException{
        var statement = "INSERT INTO GameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        //var json = new Gson().toJson(gameData);
        var id = executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        return id;
    }

    @Override
    public Map<Integer, GameData> getAllGameData() throws ResponseException{
        Map<Integer, GameData> result = new HashMap<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID,whiteUsername,blackUsername, gameName, game FROM GameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        result.put(gameID, getGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData getGame(Integer gameID) throws ResponseException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID,whiteUsername,blackUsername,gameName,game FROM GameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return getGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void updateGame(Integer gameID, String username, String color) throws ResponseException{
        if(Objects.equals(color, "WHITE")) {
            if (getGame(gameID).whiteUsername() != null) {
                throw new ResponseException(403, "Error: white player already taken");
            }
             try (var conn = DatabaseManager.getConnection()) {
                var statement = "UPDATE GameData SET whiteUsername = ? WHERE gameID = ?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
            }
        } else if (Objects.equals(color, "BLACK")) {
            if (getGame(gameID).blackUsername() != null) {
                throw new ResponseException(403, "Error: black player already taken");
            }
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "UPDATE GameData SET blackUsername = ? WHERE gameID = ?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
            }
        }
    }

    @Override
    public void clear() throws ResponseException {
        var statement = "TRUNCATE GameData";
        executeUpdate(statement);
    }

    private GameData getGameData(ResultSet rs) throws SQLException{
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String gameJson = rs.getString("game");

        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        return gameData;
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    }
                    else if (param instanceof ChessGame p) {
                        ps.setString(i + 1, new Gson().toJson(p));
                    }
                    else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
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

    private void configureDatabase() {
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
