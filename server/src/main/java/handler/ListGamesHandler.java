package handler;

import service.response.Response;
import service.exceptions.ServerErrorException;
import service.exceptions.UnauthorizedException;
import service.GameService;

public class ListGamesHandler extends Handler {
    public void handleListGames(spark.Request request, spark.Response response) {
        System.out.println("Created new ListGames Handler");

        String authToken = request.headers("authorization");

        Response serviceResponse;
        try {
            serviceResponse = GameService.getInstance().listGames(authToken);
            serialize(serviceResponse, response);
        } catch (UnauthorizedException e) {
            serializeError(e, 401, response);
        } catch (ServerErrorException e) {
            serializeError(e, 500, response);
        }
    }
}
