package serverTests.serviceTests;

import org.junit.jupiter.api.*;
import server.*;
import service.DatabaseService;
import service.UserService;

public class UserServiceTests {

    UserService userService = UserService.getInstance();
    DatabaseService databaseService = DatabaseService.getInstance();

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
    @DisplayName("Login Existing User")
    public void loginExistingUser() throws Exception {
        userService.register(new RegisterRequest("username", "password", "email"));
        LoginResponse loginResponse = (LoginResponse) userService.login(new LoginRequest("username", "password"));
        Assertions.assertEquals(loginResponse.username(), "username");
        Assertions.assertNotNull(loginResponse.authToken());

    }
}
