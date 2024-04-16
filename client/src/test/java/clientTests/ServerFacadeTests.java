package clientTests;

import api.ConnectionManager;
import api.HTTPResponseException;
import api.facade.ServerFacade;
import client.InGameMenu;
import client.Menu;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import service.DatabaseService;

import java.util.Iterator;
import java.util.Set;

public class ServerFacadeTests {

	private static Server server;
	private static ServerFacade serverFacade;

	@BeforeAll
	public static void init() throws Exception {
		server = new Server();
		var port = server.run(0);
		System.out.println("Started test HTTP server on " + port);
		serverFacade = new ServerFacade(port, new InGameMenu(new Menu()));
	}

	@AfterAll
	static void stopServer() {
		server.stop();
	}

	@BeforeEach
	public void clearDatabase() throws Exception {
		(new DatabaseService()).clear();
	}

	@BeforeEach
	public void clearAuthToken() {
		ConnectionManager.clearAuthToken();
	}

	@Test
	@DisplayName("Login Success")
	public void loginTest() throws Exception {
		serverFacade.register("username", "password", "email");
		Assertions.assertDoesNotThrow(() -> serverFacade.login("username", "password"));
		Assertions.assertFalse(ConnectionManager.getAuthToken().isEmpty());
	}

	@Test
	@DisplayName("Login with unauthorized user")
	public void loginUnauthorized() {
		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.login("username", "password"));
	}

	@Test
	@DisplayName("Register Success")
	public void registerSuccess() throws Exception {
		serverFacade.register("username", "password", "email");
		Assertions.assertDoesNotThrow(() -> serverFacade.login("username", "password"));
		Assertions.assertFalse(ConnectionManager.getAuthToken().isEmpty());
	}

	@Test
	@DisplayName("Register already taken")
	public void RegisterAlreadyTaken() throws Exception {
		serverFacade.register("username", "password", "email");
		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.register("username", "password2", "email2"));
	}

	@Test
	@DisplayName("Logout Success")
	public void logoutSuccess() throws Exception {
		serverFacade.register("username", "password", "email");
		serverFacade.login("username", "password");
		serverFacade.logout();
		Assertions.assertTrue(ConnectionManager.getAuthToken().isEmpty());
	}

	@Test
	@DisplayName("Logout Unauthorized")
	public void logoutUnauthorized() throws Exception {
		serverFacade.register("username", "password", "email");
		serverFacade.login("username", "password");
		serverFacade.logout();

		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.logout());
	}

	@Test
	@DisplayName("Create a game success")
	public void createGame() throws Exception {
		serverFacade.register("username", "password", "email");
		Assertions.assertDoesNotThrow(() -> serverFacade.createGame("gameName"));
	}

	@Test
	@DisplayName("Create a game unauthorized")
	public void createGameUnauthorized() {
		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.createGame("myGameName"));
	}

	@Test
	@DisplayName("List all Games with auth")
	public void listGamesAuthorized() throws Exception {
		serverFacade.register("username", "password", "email");
		serverFacade.createGame("gameName");
		Set<GameData> games = serverFacade.listGames();
		Iterator<GameData> it = games.iterator();
		Assertions.assertEquals(it.next().gameName(), "gameName");
	}

	@Test
	@DisplayName("List games without authorization")
	public void listGamesUnauthorized() {
		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.listGames());
	}

	@Test
	@DisplayName("Join a game with authorization")
	public void joinGameAuthorized() throws Exception {
		serverFacade.register("username", "password", "email");
		serverFacade.createGame("gameName");
		Set<GameData> games = serverFacade.listGames();

		Iterator<GameData> it = games.iterator();
		Integer gameID = it.next().gameID();

		serverFacade.joinGame("BLACK", gameID);

		games = serverFacade.listGames();

		it = games.iterator();
		Assertions.assertEquals("username", it.next().blackUsername());
	}

	@Test
	@DisplayName("Join a chess game that doesn't exist")
	public void joinChessGameNonExistent() throws Exception {
		serverFacade.register("username", "password", "email");
		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.joinGame("BLACK", 1));
	}

}
