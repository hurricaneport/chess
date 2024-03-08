package dataAccess.sql;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAuthDAO implements AuthDAO {
    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        String sql = "SELECT `username`" +
                "FROM `auth`" +
                "WHERE auth_token = ?";
        AuthData authData = null;

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String username = resultSet.getString("username");

                    authData = new AuthData(authToken, username);
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authData;
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {
        String sql = "INSERT into `auth` (auth_token, username)" +
                "values (?,?)";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());

                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not add auth data");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }
    }

    @Override
    public void deleteAuthData(AuthData authData) throws DataAccessException {
        String sql = "DELETE FROM `auth`" +
                "WHERE username = ? AND auth_token = ?";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());

                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not delete auth token");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM `auth`";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Could not clear table");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }

    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        String sql = "SELECT * FROM `auth`";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                return !resultSet.next();
            } catch (SQLException e) {
                throw new DataAccessException("Could not execute query");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }
    }
}
