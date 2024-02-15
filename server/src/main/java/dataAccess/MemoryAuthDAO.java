package dataAccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{
    @Override
    public AuthData getAuthData(String authToken) {
        return null;
    }

    @Override
    public void addAuthData(AuthData authData) {

    }

    @Override
    public void clear() {

    }
}
