package dataAccess.sql;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAO;
import dataAccess.util.PasswordUtils;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUserDAO implements UserDAO {
    private static final UserDAO userDao = new DBUserDAO();

    public static UserDAO getUserDao() {
        return userDao;
    }
    @Override
    public void addUser(UserData userData) throws DataAccessException {
        String sql = "INSERT into `user` (username, password, email)" +
                "values (?,?,?)";

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, PasswordUtils.hashPassword(userData.password()));
                preparedStatement.setString(3, userData.email());

                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not add user");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not connect to database");
        }
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public UserData getUserByEmail(String email) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
