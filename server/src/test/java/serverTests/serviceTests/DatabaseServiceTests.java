package serverTests.serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.requestResponse.LoginRequest;
import server.requestResponse.RegisterRequest;
import server.requestResponse.RegisterResponse;
import server.UnauthorizedException;
import service.DatabaseService;
import service.UserService;

public class DatabaseServiceTests {
    DatabaseService databaseService = DatabaseService.getInstance();
    UserService userService = UserService.getInstance();

    AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    UserDAO userDAO = MemoryUserDAO.getUserDAO();
    GameDAO gameDAO = MemoryGameDAO.getGameDAO();
    @BeforeEach
    public void clear() throws Exception {
        databaseService.clear();
    }

    @Test
    @DisplayName("Clear Database with entries in all tables")
    public void clearDatabase() throws Exception {
        RegisterResponse registerResponse = (RegisterResponse) userService.register(new RegisterRequest("username", "password", "email"));
        databaseService.clear();
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("username", "password")));
        Assertions.assertTrue(authDAO.isEmpty());
        Assertions.assertTrue(userDAO.isEmpty());
        Assertions.assertTrue(gameDAO.isEmpty());
    }
}
