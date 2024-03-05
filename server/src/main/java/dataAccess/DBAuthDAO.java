package dataAccess;

import model.AuthData;

public class DBAuthDAO implements AuthDAO{
    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {

    }

    @Override
    public void deleteAuthData(AuthData authData) throws DataAccessException {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
