package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.sql.DBAuthDAO;
import org.junit.jupiter.api.*;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;
import model.request.LoginRequest;
import model.response.LoginResponse;
import model.request.RegisterRequest;
import model.response.RegisterResponse;
import service.DatabaseService;
import service.UserService;

public class UserServiceTests {

    final UserService userService = UserService.getInstance();
    final DatabaseService databaseService = DatabaseService.getInstance();

    final AuthDAO authDAO = DBAuthDAO.getAuthDAO();

    @BeforeEach
    public void clearDB() throws Exception {
        databaseService.clear();
    }

    @Test
    @DisplayName("Create new user")
    public void createNewUser() throws Exception {
        RegisterResponse registerResponse = (RegisterResponse) userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertEquals(registerResponse.username(), "username");
        Assertions.assertNotNull(registerResponse.authToken());
    }

    @Test
    @DisplayName("Create already taken user with same username")
    public void createAlreadyTakenUserWithSameUsername() throws Exception {
        userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(new RegisterRequest("username","password2", "email2")) );
    }

    @Test
    @DisplayName("Create already taken user with same email")
    public void createAlreadyTakenUserWithSameEmail() throws Exception {
        userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(new RegisterRequest("username2","password2", "email")) );
    }

    @Test
    @DisplayName("Create user with null username")
    public void createUserWithNullUsername() {
        Assertions.assertThrows(BadRequestException.class, () -> userService.register(new RegisterRequest(null, "password", "email")));
    }

    @Test
    @DisplayName("Login Existing User")
    public void loginExistingUser() throws Exception {
        userService.register(new RegisterRequest("username", "password", "email"));
        LoginResponse loginResponse = (LoginResponse) userService.login(new LoginRequest("username", "password"));
        Assertions.assertEquals(loginResponse.username(), "username");
        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    @DisplayName("Login non-existent user")
    public void loginNonExistentUser() throws Exception {
        userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("username2", "password")));
    }

    @Test
    @DisplayName("Login with incorrect password")
    public void incorrectPassword() throws Exception {
        userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("username", "password2")));
    }

    @Test
    @DisplayName("Logout user")
    public void logoutUser() throws Exception {
        RegisterResponse registerResponse = (RegisterResponse) userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertDoesNotThrow(() -> userService.logout(registerResponse.authToken()));
        Assertions.assertNull(authDAO.getAuthData(registerResponse.authToken()));
    }

    @Test
    @DisplayName("Logout user with bad auth token")
    public void logoutUserWithBadAuth() throws Exception {
        RegisterResponse registerResponse = (RegisterResponse) userService.register(new RegisterRequest("username", "password", "email"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(registerResponse.authToken());
        stringBuilder.reverse();
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(stringBuilder.toString()));
        Assertions.assertNotNull(authDAO.getAuthData(registerResponse.authToken()));
    }
}
