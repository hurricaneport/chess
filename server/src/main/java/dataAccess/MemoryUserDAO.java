package dataAccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{

    HashSet<UserData> userTable = new HashSet<>();

    @Override
    public boolean addUser(UserData userData) throws DataAccessException {
        return userTable.add(userData);
    }

    @Override
    public UserData getUser(String username) {
        for (UserData userData : userTable) {
            if (userData.username().equals(username)) {
                return userData;
            }
        }

        return null;
    }

    @Override
    public void clear() {
        userTable = new HashSet<>();
    }
}
