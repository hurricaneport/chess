package handler;

import service.exceptions.BadRequestException;
import service.exceptions.ServerErrorException;
import service.exceptions.UnauthorizedException;
import model.request.CreateGameRequest;
import model.response.Response;
import service.GameService;

public class CreateGameHandler extends Handler {
    public void handleCreateGame(spark.Request request, spark.Response response) {
        System.out.println("Created new CreateGamesHandler");

        CreateGameRequest createGameRequest = deserialize(request, CreateGameRequest.class);

        String authToken = request.headers("authorization");

        Response serviceResponse;
        try {
            serviceResponse = GameService.getInstance().createGame(authToken, createGameRequest);
            serialize(serviceResponse, response);
        } catch (UnauthorizedException e) {
            serializeError(e, 401, response);
        } catch (BadRequestException e) {
            serializeError(e, 400, response);
        } catch (ServerErrorException e) {
            serializeError(e, 500, response);
        }
    }
}
