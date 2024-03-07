package serverTests.serviceTests;

import dataAccess.*;
import dataAccess.memory.MemoryAuthDAO;
import dataAccess.memory.MemoryGameDAO;
import dataAccess.memory.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.exceptions.UnauthorizedException;
import service.DatabaseService;
import service.UserService;

public class DatabaseServiceTests {
    final DatabaseService databaseService = DatabaseService.getInstance();
    final UserService userService = UserService.getInstance();

    final AuthDAO authDAO = MemoryAuthDAO.getAuthDAO();
    final UserDAO userDAO = MemoryUserDAO.getUserDAO();
    final GameDAO gameDAO = MemoryGameDAO.getGameDAO();
    @BeforeEach
    public void clear() throws Exception {
        databaseService.clear();
    }

    @Test
    @DisplayName("Clear Database with entries in all tables")
    public void clearDatabase() throws Exception {
        userService.register(new RegisterRequest("username", "password", "email"));
        databaseService.clear();
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("username", "password")));
        Assertions.assertTrue(authDAO.isEmpty());
        Assertions.assertTrue(userDAO.isEmpty());
        Assertions.assertTrue(gameDAO.isEmpty());
    }
}
