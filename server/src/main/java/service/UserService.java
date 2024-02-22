package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import server.ErrorResponse;
import server.RegisterRequest;
import server.RegisterResponse;
import server.Response;

import java.util.UUID;

public class UserService extends Service {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    static UserService staticUserService = new UserService();
    public static UserService getInstance() {
        return staticUserService;
    }

    /**
     * Service to register a user
     * @param registerRequest Request object containing the registration information
     * @return Response object containing response information
     */
    public Response register(RegisterRequest registerRequest) {
        if (userExists(registerRequest.username())) {
            System.out.println("Error: user " + registerRequest.username() + " already taken.");
            return new ErrorResponse("Error: already taken", 403);
        }

        UserData userData = new UserData(registerRequest.username(),registerRequest.password(),registerRequest.email());
        try {
            userDAO.addUser(userData);
        }
        catch (DataAccessException e) {
            System.out.println("Exception" + e);
            return new ErrorResponse("Error: internal database error", 500);
        }

        AuthData authdata = createAuth(registerRequest.username());
        return new RegisterResponse(registerRequest.username(), authdata.authToken());
    }

    /**
     * @param username username to check
     * @return true if user exists
     */
    private boolean userExists(String username) {
        return userDAO.getUser(username) != null;
    }

    /**
     * Creates an authToken for given user and adds it to authTable
     * @param username username to create authToken for
     * @return authToken for user
     */
    private AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        try {
            authDAO.addAuthData(authData);
        }
        catch (DataAccessException e) {
            System.out.println("Error" + e);
            authData = null;
        }
        return authData;
    }
}
