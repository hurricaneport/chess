package server;

import server.handler.*;
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
            (new ClearHandler()).handleClear(response);
            return response.body();
        }));

        Spark.delete("/session", ((request, response) -> {
            (new LogoutHandler()).handleLogout(request, response);
            return response.body();
        }));

        Spark.post("/game", ((request, response) -> {
            (new CreateGameHandler()).handleCreateGame(request, response);
            return response.body();
        }));

        Spark.get("/game", ((request, response) -> {
            (new ListGamesHandler()).handleListGames(request, response);
            return response.body();
        }));
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
