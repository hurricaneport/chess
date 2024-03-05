package service;

import dataAccess.*;
import dataAccess.memory.MemoryAuthDAO;
import dataAccess.memory.MemoryGameDAO;
import dataAccess.memory.MemoryUserDAO;
import server.exceptions.ServerErrorException;


public class DatabaseService extends Service {
    private static final DatabaseService databaseService = new DatabaseService();
    AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    UserDAO userDAO = MemoryUserDAO.getUserDAO();
    GameDAO gameDAO = MemoryGameDAO.getGameDAO();

    public static DatabaseService getInstance() {
        return databaseService;
    }
    public void clear() throws ServerErrorException {
        try {
            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();
        }
        catch (DataAccessException e) {
            throw new ServerErrorException("Error: " + e);
        }

    }
}