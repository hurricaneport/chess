package serverTests.serviceTests;

import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.*;
import service.DatabaseService;
import service.GameService;
import service.UserService;

public class GameServiceTests {
    private final DatabaseService databaseService = DatabaseService.getInstance();
    private final GameService gameService = GameService.getInstance();
    private final UserService userService = UserService.getInstance();

    private final GameDAO gameDAO = MemoryGameDAO.getGameDAO();

    private AuthData authData;

    @BeforeEach
    @Order(0)
    public void clear() throws Exception {
        databaseService.clear();
    }

    @BeforeEach
    @Order(1)
    public void createUser() throws Exception {
        RegisterResponse registerResponse = (RegisterResponse) userService.register(new RegisterRequest("username", "password", "email"));
        authData = new AuthData(registerResponse.authToken(), registerResponse.username());
    }

    @Test
    @DisplayName("Create valid game")
    public void createValidGame() throws Exception {
        CreateGameResponse createGameResponse = (CreateGameResponse) gameService.createGame(authData.authToken(), new CreateGameRequest("My game"));
        Assertions.assertNotNull(createGameResponse);
        Assertions.assertNotNull(gameDAO.getGame(createGameResponse.gameID()));
    }

    @Test
    @DisplayName("Create valid game without authorization")
    public void createGameWithoutAuth() {
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.createGame("1234", new CreateGameRequest("My game")));
    }

    @Test
    @DisplayName("Create invalid game")
    public void createInvalidGame() {
        Assertions.assertThrows(BadRequestException.class, () -> gameService.createGame(authData.authToken(), new CreateGameRequest(null)));
    }

    @Test
    @DisplayName("List all games")
    public void listALlGames() {

    }
}
