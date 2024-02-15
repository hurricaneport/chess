package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {

    /**
     * Adds a user to the database
     * @param userData UserData to be added
     * @return true if new user created, false otherwise
     * @throws DataAccessException if user with username or email already exists
     */
    boolean addUser(UserData userData) throws DataAccessException;

    /**
     * Gets a user by username
     * @param username username of the user to search for
     * @return UserData object if user found, otherwise null
     */
    UserData getUser(String username);

    /**
     * Clears User table of DB
     */
    void clear();
}
