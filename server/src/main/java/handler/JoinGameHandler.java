package handler;

import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;
import server.exceptions.ServerErrorException;
import server.exceptions.UnauthorizedException;
import service.request.JoinGameRequest;
import service.GameService;

public class JoinGameHandler extends Handler{
    public void handleJoinGame(spark.Request request, spark.Response response) {
        System.out.println("Created new JoinGameHandler");

        JoinGameRequest joinGameRequest = deserialize(request, JoinGameRequest.class);

        String authToken = request.headers("authorization");

        try {
            GameService.getInstance().joinGame(authToken, joinGameRequest);
            response.body("");
            response.status(200);
        }
        catch (UnauthorizedException e) {
            serializeError(e, 401, response);
        }
        catch (BadRequestException e) {
            serializeError(e, 400, response);
        }
        catch (ServerErrorException e) {
            serializeError(e, 500, response);
        }
        catch (AlreadyTakenException e) {
            serializeError(e, 403, response);
        }
    }
}