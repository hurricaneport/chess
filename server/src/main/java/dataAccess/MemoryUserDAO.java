package dataAccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{

    private static final MemoryUserDAO userDAO = new MemoryUserDAO();

    HashSet<UserData> userTable = new HashSet<>();

    public static MemoryUserDAO getUserDAO() {
        return userDAO;
    }

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
    public UserData getUserByEmail(String email) {
        for (UserData userData : userTable) {
            if (userData.email().equals(email)) {
                return userData;
            }
        }

        return null;
    }

    @Override
    public void clear() {
        userTable = new HashSet<>();
    }

    @Override
    public boolean isEmpty() {
        return userTable.isEmpty();
    }
}
