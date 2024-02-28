package service;

import dataAccess.*;
import server.ServerErrorException;


public class DatabaseService extends Service {
    private static final DatabaseService databaseService = new DatabaseService();
    AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    UserDAO userDAO = MemoryUserDAO.getUserDAO();
    GameDAO gameDAO = MemoryGameDAO.getGameDAO();

    public static DatabaseService getInstance() {
        return databaseService;
    }
    public void clear() throws ServerErrorException {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();

    }
}