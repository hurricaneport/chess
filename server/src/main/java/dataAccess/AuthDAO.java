package dataAccess;

import model.AuthData;


public interface AuthDAO {
    /**
     * Gets authorization given an auth token
     * @param authToken auth token to check for
     * @return returns AuthData if authToken found, otherwise returns null
     */
    AuthData getAuthData(String authToken) throws DataAccessException;

    /**
     * Adds given auth data to the database
     * @param authData AuthData to be added to database
     */
    void addAuthData(AuthData authData) throws DataAccessException;

    void deleteAuthData(AuthData authData) throws DataAccessException;

    /**
     * Clears Auth Table of DB
     */
    void clear() throws DataAccessException;

    /**
     * @return true if auth table is empty, false otherwise
     */
    boolean isEmpty() throws DataAccessException;
}
