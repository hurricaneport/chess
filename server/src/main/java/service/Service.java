package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import server.ServerErrorException;

public abstract class Service {
    private final AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    public AuthData authorize(String authToken) throws ServerErrorException {
        try {
            return authDAO.getAuthData(authToken);
        } catch (DataAccessException e) {
            throw new ServerErrorException("Error: internal database error");
        }
    }
}
