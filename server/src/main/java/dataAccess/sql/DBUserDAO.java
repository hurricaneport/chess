package dataAccess.sql;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;

public class DBUserDAO implements UserDAO {
    @Override
    public boolean addUser(UserData userData) throws DataAccessException {
        return false;
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
