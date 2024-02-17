package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{

    private HashSet<AuthData> authTable = new HashSet<>();
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
