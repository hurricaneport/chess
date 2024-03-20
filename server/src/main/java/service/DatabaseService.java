package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.sql.DBAuthDAO;
import dataAccess.sql.DBGameDAO;
import dataAccess.sql.DBUserDAO;
import service.exceptions.ServerErrorException;

public class DatabaseService extends Service {
	private static final DatabaseService databaseService = new DatabaseService();
	final AuthDAO authDAO = DBAuthDAO.getAuthDAO();
	final UserDAO userDAO = DBUserDAO.getUserDAO();
	final GameDAO gameDAO = DBGameDAO.getGameDAO();

	public static DatabaseService getInstance() {
		return databaseService;
	}

	public void clear() throws ServerErrorException {
		try {
			authDAO.clear();
			userDAO.clear();
			gameDAO.clear();
		} catch (DataAccessException e) {
			throw new ServerErrorException("Error: " + e);
		}

	}
}