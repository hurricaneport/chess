package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{

    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    private HashSet<AuthData> authTable = new HashSet<>();

    public static MemoryAuthDAO getAuthDAO() {
        return authDAO;
    }
    @Override
    public AuthData getAuthData(String authToken) {
        for (AuthData authData : authTable) {
            if (Objects.equals(authData.authToken(), authToken)) {
                return authData;
            }
        }

        return null;
    }

    @Override
    public void addAuthData(AuthData authData) throws DataAccessException {
        authTable.add(authData);
    }

    @Override
    public void clear() {
        authTable = new HashSet<>();
    }
}
