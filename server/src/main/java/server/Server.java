package server;

import server.handler.ClearHandler;
import server.handler.LoginHandler;
import server.handler.LogoutHandler;
import server.handler.RegisterHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.get("/game", ((request, response) -> "Hello World"));
        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void createRoutes() {
        Spark.post("/user", ((request, response) -> {
            (new RegisterHandler()).handleRegistration(request, response);
            return response.body();
        }));

        Spark.post("/session", ((request, response) -> {
            (new LoginHandler()).handleLogin(request, response);
            return response.body();
        }));

        Spark.delete("/db", ((request, response) -> {
            (new ClearHandler()).handleClear(request, response);
            return response.body();
        }));

        Spark.delete("/session", ((request, response) -> {
            (new LogoutHandler()).handleLogout(request, response);
            return response.body();
        }));
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
