package clientTests;

import api.ServerFacade;
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

    @BeforeEach
    public void clearDatabase() throws Exception{
        (new DatabaseService()).clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Login")
    public void loginTest() throws Exception {
        serverFacade.register(new RegisterRequest("username", "password", "email"));
        //Assertions.assertDoesNotThrow(() -> serverFacade.login(new LoginRequest("username", "password")));
    }

}
