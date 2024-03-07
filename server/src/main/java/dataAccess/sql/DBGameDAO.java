package dataAccess.sql;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import dataAccess.util.JsonUtils;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DBGameDAO implements GameDAO {
    static GameDAO gameDAO = new DBGameDAO();

    public static GameDAO getGameDAO() {
        return gameDAO;
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM game";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
    public boolean isEmpty() throws DataAccessException {
        String sql = "SELECT * FROM `game`";

        try(Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                return !resultSet.next();
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not execute query");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }
    }

    @Override
    public Set<GameData> getGames() throws DataAccessException {
        String sql = "SELECT `game_id`, `white_username`, `black_username`, `game_name`, `game`" +
                "FROM `game`";

        Set<GameData> games = new HashSet<>();

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int gameID = resultSet.getInt("game_id");
                    String whiteUsername = resultSet.getString("white_username");
                    String blackUsername = resultSet.getString("black_username");
                    String gameName = resultSet.getString("game_name");
                    ChessGame game = JsonUtils.deserializeChessGame(resultSet.getString("game"));
                    GameData currentGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    games.add(currentGame);
                }
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not execute query");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }

        return games;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT `white_username`, `black_username`, `game_name`, `game`" +
                "FROM `game`" +
                "WHERE `game_id` = ?";

        GameData gameData = null;

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, gameID);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String whiteUsername = resultSet.getString(1);
                    String blackUsername = resultSet.getString(2);
                    String gameName = resultSet.getString(3);
                    ChessGame game = JsonUtils.deserializeChessGame(resultSet.getString(4));

                    gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                }
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not execute query");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }

        return gameData;
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT into `game` (game_name, game)" +
                "values (?,?) ";

        int gameID;
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
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
            catch (SQLException e) {
                throw  new DataAccessException("Could not create game");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }

        return gameID;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        String sql = "UPDATE `game` " +
                "SET `white_username` = ?, `black_username` = ?, game_name = ?, game = ?" +
                "WHERE `game_id` = ?";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                preparedStatement.setString(4, JsonUtils.serializeChessGame(gameData.game()));
                preparedStatement.setInt(5, gameID);

                int rows = preparedStatement.executeUpdate();
                if (rows == 0) {
                    throw new DataAccessException("Could not find game with given ID");
                }
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not execute update");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }

    }
}
