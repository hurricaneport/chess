package dataAccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    @Override
    public boolean addUser(UserData userData) throws DataAccessException {
        return false;
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clear() {

    }
}
