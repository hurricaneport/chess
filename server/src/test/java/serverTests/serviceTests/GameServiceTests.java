package serverTests.serviceTests;

import chess.ChessGame;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.*;
import server.requestResponse.*;
import service.DatabaseService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

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
    public void listALlGames() throws Exception {
        CreateGameResponse createGameResponse1 = (CreateGameResponse) gameService.createGame(authData.authToken(), new CreateGameRequest("game 1"));
        CreateGameResponse createGameResponse2 = (CreateGameResponse) gameService.createGame(authData.authToken(), new CreateGameRequest("game 2"));
        CreateGameResponse createGameResponse3 = (CreateGameResponse) gameService.createGame(authData.authToken(), new CreateGameRequest("game 3"));
        GameData gameData1 = new GameData(createGameResponse1.gameID(), null, null, "game 1", new ChessGame());
        GameData gameData2 = new GameData(createGameResponse2.gameID(), null, null, "game 2", new ChessGame());
        GameData gameData3 = new GameData(createGameResponse3.gameID(), null, null, "game 3", new ChessGame());

        ArrayList<GameData> gameDataArrayList = new ArrayList<>();

        gameDataArrayList.add(gameData1);
        gameDataArrayList.add(gameData2);
        gameDataArrayList.add(gameData3);

        ListGamesResponse listGamesResponse = (ListGamesResponse) gameService.listGames(authData.authToken());

        Assertions.assertEquals(gameDataArrayList, listGamesResponse.games());




    }
    @Test
    @DisplayName("List games when no games added")
    public void listGamesWhenNoGames() throws Exception {
        ListGamesResponse listGamesResponse = (ListGamesResponse) gameService.listGames(authData.authToken());
        Assertions.assertEquals(listGamesResponse.games(), new ArrayList<>());
    }

    @Test
    @DisplayName("List games when unauthorized")
    public void listGamesUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames("1234"));
    }

    @Test
    @DisplayName("Join a game as a player")
    public void joinGameAsPlayer() throws Exception {
        CreateGameResponse createGameResponse = (CreateGameResponse) gameService.createGame(authData.authToken(), new CreateGameRequest("game"));
        gameService.joinGame(authData.authToken(), new JoinGameRequest("WHITE", createGameResponse.gameID()));
        ListGamesResponse listGamesResponse = (ListGamesResponse) gameService.listGames(authData.authToken());
        Assertions.assertEquals(listGamesResponse.games().getFirst(), new GameData(createGameResponse.gameID(), authData.username(), null, "game", new ChessGame()));
    }

    @Test
    @DisplayName("Join a game that doesn't exist")
    public void joinGameThatDoesNotExist() {
        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(authData.authToken(), new JoinGameRequest("WHITE", 99)));
    }
}



