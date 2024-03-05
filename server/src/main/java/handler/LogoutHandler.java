package handler;

import service.exceptions.ServerErrorException;
import service.exceptions.UnauthorizedException;
import service.UserService;

public class LogoutHandler extends Handler {
    public void handleLogout(spark.Request request, spark.Response response) {
        System.out.println("Created new Logout Handler");

        String authToken = request.headers("authorization");

        try {
            UserService.getInstance().logout(authToken);
            response.status(200);
            response.body("");
        }
        catch (UnauthorizedException e) {
            serializeError(e, 401, response);
        }
        catch (ServerErrorException e) {
            serializeError(e, 500, response);
        }
    }
}
