package clientTests;

import api.HTTPConnectionManager;
import api.HTTPResponseException;
import api.ServerFacade;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import service.DatabaseService;

public class ServerFacadeTests {

	private static Server server;
	private static ServerFacade serverFacade;

	@BeforeAll
	public static void init() {
		server = new Server();
		var port = server.run(0);
		System.out.println("Started test HTTP server on " + port);
		serverFacade = new ServerFacade(port);
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
		HTTPConnectionManager.clearAuthToken();
	}

	@Test
	@DisplayName("Login Success")
	public void loginTest() throws Exception {
		serverFacade.register(new RegisterRequest("username", "password", "email"));
		Assertions.assertDoesNotThrow(() -> serverFacade.login(new LoginRequest("username", "password")));
		Assertions.assertFalse(HTTPConnectionManager.getAuthToken().isEmpty());
	}

	@Test
	@DisplayName("Login with unauthorized user")
	public void loginUnauthorized() {
		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.login(new LoginRequest("username", "password")));
	}

	@Test
	@DisplayName("Register Success")
	public void registerSuccess() throws Exception {
		serverFacade.register(new RegisterRequest("username", "password", "email"));
		Assertions.assertDoesNotThrow(() -> serverFacade.login(new LoginRequest("username", "password")));
		Assertions.assertFalse(HTTPConnectionManager.getAuthToken().isEmpty());
	}

	@Test
	@DisplayName("Register already taken")
	public void RegisterAlreadyTaken() throws Exception {
		serverFacade.register(new RegisterRequest("username", "password", "email"));
		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.register(new RegisterRequest("username", "password2", "email2")));
	}

	@Test
	@DisplayName("Logout Success")
	public void logoutSuccess() throws Exception {
		serverFacade.register(new RegisterRequest("username", "password", "email"));
		serverFacade.login(new LoginRequest("username", "password"));
		serverFacade.logout();
		Assertions.assertTrue(HTTPConnectionManager.getAuthToken().isEmpty());
	}

	@Test
	@DisplayName("Logout Unauthorized")
	public void logoutUnauthorized() throws Exception {
		serverFacade.register(new RegisterRequest("username", "password", "email"));
		serverFacade.login(new LoginRequest("username", "password"));
		serverFacade.logout();

		Assertions.assertThrows(HTTPResponseException.class, () -> serverFacade.logout());
	}

}
