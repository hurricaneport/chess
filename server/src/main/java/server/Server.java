package server;

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
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
