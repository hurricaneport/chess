package dataAccess.sql;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import dataAccess.util.JsonUtils;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBGameDAO implements GameDAO {

    static GameDAO gameDAO = new DBGameDAO();

    public static GameDAO getGameDAO() {
        return gameDAO;
    }
    private void createTable() throws DataAccessException {

        String createTable = "CREATE TABLE IF NOT EXISTS `game` (\n" +
                "  `game_id` int NOT NULL AUTO_INCREMENT,\n" +
                "  `game_name` varchar(45) NOT NULL,\n" +
                "  `white_username` varchar(45) DEFAULT NULL,\n" +
                "  `black_username` varchar(45) DEFAULT NULL,\n" +
                "  `game` mediumtext NOT NULL,\n" +
                "  PRIMARY KEY (`game_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTable)) {
                int updateCount = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Could not create table: game");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not open database");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        createTable();

        String clearTableStatement = "DELETE FROM game";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(clearTableStatement)) {
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not delete table: game");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not open database");
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public List<GameData> getGames() {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        String createGameString = "INSERT into `game` (game_name, game)" +
                "values (?,?) ";

        int gameID;
        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(createGameString, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gameData.gameName());
                preparedStatement.setString(2, JsonUtils.serializeChessGame(gameData.game()));

                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    generatedKeys.next();
                    gameID = generatedKeys.getInt(1);
                }
                catch (SQLException e) {
                    throw new DataAccessException("Could not get game ID");
                }
            }

            connection.commit();
        }
        catch (SQLException e) {
            try {
                if (!connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new DataAccessException(e.getMessage());
            }
            throw new DataAccessException(e.getMessage());
        }

        return gameID;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {

    }
}
