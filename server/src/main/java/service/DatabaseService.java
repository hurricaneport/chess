package service;

import dataAccess.*;
import dataAccess.memory.MemoryAuthDAO;
import dataAccess.memory.MemoryGameDAO;
import dataAccess.memory.MemoryUserDAO;
import service.exceptions.ServerErrorException;


public class DatabaseService extends Service {
    private static final DatabaseService databaseService = new DatabaseService();
    final AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    final UserDAO userDAO = MemoryUserDAO.getUserDAO();
    final GameDAO gameDAO = MemoryGameDAO.getGameDAO();

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