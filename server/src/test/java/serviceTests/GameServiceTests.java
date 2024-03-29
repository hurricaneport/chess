package serviceTests;

import chess.ChessGame;
import dataAccess.GameDAO;
import dataAccess.sql.DBGameDAO;
import model.AuthData;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.RegisterRequest;
import model.response.CreateGameResponse;
import model.response.ListGamesResponse;
import model.response.RegisterResponse;
import org.junit.jupiter.api.*;
import service.DatabaseService;
import service.GameService;
import service.UserService;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import java.util.HashSet;

public class GameServiceTests {
	private final DatabaseService databaseService = DatabaseService.getInstance();
	private final GameService gameService = GameService.getInstance();
	private final UserService userService = UserService.getInstance();

	private final GameDAO gameDAO = DBGameDAO.getGameDAO();

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

		HashSet<GameData> gameDataHashSet = new HashSet<>();

		gameDataHashSet.add(gameData1);
		gameDataHashSet.add(gameData2);
		gameDataHashSet.add(gameData3);

		ListGamesResponse listGamesResponse = (ListGamesResponse) gameService.listGames(authData.authToken());

		Assertions.assertEquals(gameDataHashSet, listGamesResponse.games());

	}

	@Test
	@DisplayName("List games when no games added")
	public void listGamesWhenNoGames() throws Exception {
		ListGamesResponse listGamesResponse = (ListGamesResponse) gameService.listGames(authData.authToken());
		Assertions.assertEquals(listGamesResponse.games(), new HashSet<>());
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
		Assertions.assertEquals(listGamesResponse.games().iterator().next(), new GameData(createGameResponse.gameID(), authData.username(), null, "game", new ChessGame()));
	}

	@Test
	@DisplayName("Join a game that doesn't exist")
	public void joinGameThatDoesNotExist() {
		Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(authData.authToken(), new JoinGameRequest("WHITE", 99)));
	}
}



