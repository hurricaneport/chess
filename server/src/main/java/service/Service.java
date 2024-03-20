package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.sql.DBAuthDAO;
import model.AuthData;
import service.exceptions.ServerErrorException;

public abstract class Service {
	private final AuthDAO authDAO = DBAuthDAO.getAuthDAO();

	public AuthData authorize(String authToken) throws ServerErrorException {
		try {
			return authDAO.getAuthData(authToken);
		} catch (DataAccessException e) {
			throw new ServerErrorException("Error: internal database error");
		}
	}
}
