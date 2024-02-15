package dataAccess;

import model.AuthData;

public interface AuthDAO {
    /**
     * Gets authorization given an auth token
     * @param authToken authtoken to check for
     * @return returns AuthData if authToken found, otherwise returns null
     */
    AuthData getAuthData(String authToken);

    /**
     * Adds given authdata to the database
     * @param authData AuthData to be added to database
     */
    void addAuthData(AuthData authData);

    /**
     * Clears Auth Table of DB
     */
    void clear();
}
