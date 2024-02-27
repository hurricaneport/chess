package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import server.ServerErrorException;


public class DatabaseService extends Service {
    private static final DatabaseService databaseService = new DatabaseService();
    AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    UserDAO userDAO = MemoryUserDAO.getUserDAO();

    public static DatabaseService getInstance() {
        return databaseService;
    }
    public void clear() throws ServerErrorException {
        authDAO.clear();
        userDAO.clear();

    }
}