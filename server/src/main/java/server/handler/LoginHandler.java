package server.handler;

import server.*;
import server.requestResponse.LoginRequest;
import server.requestResponse.Response;
import service.UserService;

public class LoginHandler extends Handler {
    public void handleLogin(spark.Request request, spark.Response response) {
        System.out.println("Created new Login Handler");

        LoginRequest serviceRequest = deserialize(request, LoginRequest.class);

        Response serviceResponse;
        try {
            serviceResponse = UserService.getInstance().login(serviceRequest);
            serialize(serviceResponse, response);
        }
        catch (BadRequestException e) {
            serializeError(e, 400, response);
        }
        catch (UnauthorizedException e) {
            serializeError(e, 401, response);
        }
        catch (ServerErrorException e) {
            serializeError(e, 500, response);
        }
    }
}
