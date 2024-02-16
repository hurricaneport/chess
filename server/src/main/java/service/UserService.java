package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import server.RegisterRequest;
import server.RegisterResponse;

import java.util.UUID;

public class UserService extends Service {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userExists(registerRequest.username())) {
            return new RegisterResponse(null,null);
        }

        UserData userData = new UserData(registerRequest.username(),registerRequest.password(),registerRequest.email());
        try {
            userDAO.addUser(userData);
        }
        catch (DataAccessException e) {
            System.out.println("Exception" + e);
            return new RegisterResponse(null, null);
        }

        AuthData authdata = createAuth(registerRequest.username());
        return new RegisterResponse(registerRequest.username(), authdata.authToken());
    }

    private boolean userExists(String username) {
        return userDAO.getUser(username) != null;
    }

    private AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDAO.addAuthData(authData);
        return authData;
    }
}
