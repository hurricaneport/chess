package server.handler;

import server.*;
import service.UserService;

public class LoginHandler extends Handler {
    public void handleLogin(spark.Request request, spark.Response response) {
        System.out.println("Created new Register Handler");

        LoginRequest serviceRequest = deserialize(request, LoginRequest.class);

        Response serviceResponse = null;
        try {
            serviceResponse = UserService.getInstance().login(serviceRequest);
            serialize(serviceResponse, response);
        } catch (UnauthorizedException e) {
            serializeError(e, 401, response);
        } catch (ServerErrorException e) {
            serializeError(e, 500, response);
        }
    }
}
