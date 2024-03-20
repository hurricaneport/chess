package clientTests;

import api.ServerFacade;
import model.request.LoginRequest;
import model.response.LoginResponse;
import org.junit.jupiter.api.*;
import server.Server;


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


    @Test
    @DisplayName("Login")
    public void sampleTest() throws Exception {
        Assertions.assertDoesNotThrow(() -> serverFacade.login(new LoginRequest("username", "password")));
    }

}
