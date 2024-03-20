package dataAccess;

import model.UserData;

public interface UserDAO {

	/**
	 * Adds a user to the database
	 *
	 * @param userData UserData to be added
	 * @throws DataAccessException if user with username or email already exists
	 */
	void addUser(UserData userData) throws DataAccessException;

	/**
	 * Gets a user by username
	 *
	 * @param username username of the user to search for
	 * @return UserData object if user found, otherwise null
	 */
	UserData getUser(String username) throws DataAccessException;

	UserData getUserByEmail(String email) throws DataAccessException;

	/**
	 * Clears User table of DB
	 */
	void clear() throws DataAccessException;

	/**
	 * @return true if user table is empty, false otherwise
	 */
	boolean isEmpty() throws DataAccessException;
}
