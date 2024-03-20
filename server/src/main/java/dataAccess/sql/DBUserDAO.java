package dataAccess.sql;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAO;
import dataAccess.util.PasswordUtils;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUserDAO implements UserDAO {
	private static final UserDAO userDao = new DBUserDAO();

	public static UserDAO getUserDAO() {
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
			} catch (SQLException e) {
				throw new DataAccessException("Could not add user");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not connect to database");
		}
	}

	@Override
	public UserData getUser(String username) throws DataAccessException {
		String sql = "SELECT `password`, `email`" +
				"FROM `user`" +
				"WHERE username = ?";
		UserData userData = null;

		try (Connection connection = DatabaseManager.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, username);

				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					String password = resultSet.getString("password");
					String email = resultSet.getString("email");

					userData = new UserData(username, password, email);
				}
			} catch (SQLException e) {
				throw new DataAccessException("Could not retrieve user");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not connect to database");
		}
		return userData;
	}

	@Override
	public UserData getUserByEmail(String email) throws DataAccessException {
		String sql = "SELECT `username`, `password`" +
				"FROM `user`" +
				"WHERE email = ?";
		UserData userData = null;

		try (Connection connection = DatabaseManager.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, email);

				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					String password = resultSet.getString("password");
					String username = resultSet.getString("username");

					userData = new UserData(username, password, email);
				}
			} catch (SQLException e) {
				throw new DataAccessException("Could not retrieve user");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not connect to database");
		}
		return userData;
	}

	@Override
	public void clear() throws DataAccessException {
		String sql = "DELETE FROM `user`";

		try (Connection connection = DatabaseManager.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				throw new DataAccessException("Error: " + e);
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error" + e);
		}
	}

	@Override
	public boolean isEmpty() throws DataAccessException {
		String sql = "SELECT * FROM `user`";

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
