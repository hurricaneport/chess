package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import server.*;

import java.util.Objects;
import java.util.UUID;

public class UserService extends Service {
    UserDAO userDAO = MemoryUserDAO.getUserDAO();
    AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    static UserService staticUserService = new UserService();
    public static UserService getInstance() {
        return staticUserService;
    }

    /**
     * Service to register a user
     * @param registerRequest Request object containing the registration information
     * @return Response object containing response information
     */
    public Response register(RegisterRequest registerRequest) throws AlreadyTakenException, ServerErrorException {
        if (userExists(registerRequest.username(),registerRequest.email())) {
            System.out.println("Error: user " + registerRequest.username() + " already taken.");
            throw new AlreadyTakenException("Error: already taken");
        }

        UserData userData = new UserData(registerRequest.username(),registerRequest.password(),registerRequest.email());
        try {
            userDAO.addUser(userData);
        }
        catch (DataAccessException e) {
            System.out.println("Exception" + e);
            throw new ServerErrorException("Error: internal database error");
        }

        AuthData authdata;
        try {
            authdata = createAuth(registerRequest.username());
        } catch (DataAccessException e) {
            throw new ServerErrorException("Error: internal database error");
        }
        return new RegisterResponse(registerRequest.username(), authdata.authToken());
    }

    public Response login(LoginRequest loginRequest) throws UnauthorizedException, ServerErrorException {
        UserData user = userDAO.getUser(loginRequest.username());
        if (user == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        if (!Objects.equals(user.password(), loginRequest.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData;
        try {
            authData = createAuth(loginRequest.username());
        } catch (DataAccessException e) {
            throw new ServerErrorException("Error: internal database error");
        }

        return new LoginResponse(authData.username(), authData.authToken());
    }

    /**
     * @param username username to check
     * @param email email to check
     * @return true if user exists
     */
    private boolean userExists(String username, String email) {
        return userDAO.getUser(username) != null || userDAO.getUserByEmail(email) != null;
    }

    private boolean userExists(String username) {
        return userDAO.getUser(username) != null;
    }

    /**
     * Creates an authToken for given user and adds it to authTable
     * @param username username to create authToken for
     * @return authToken for user
     */
    private AuthData createAuth(String username) throws DataAccessException{
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDAO.addAuthData(authData);

        return authData;
    }
}
