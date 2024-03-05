package handler;

import server.exceptions.AlreadyTakenException;
import server.exceptions.BadRequestException;
import server.exceptions.ServerErrorException;
import service.request.RegisterRequest;
import service.response.Response;
import service.UserService;

public class RegisterHandler extends Handler {
    public void handleRegistration(spark.Request request, spark.Response response) {
        System.out.println("Created new Register Handler");

        RegisterRequest registerRequest = deserialize(request, RegisterRequest.class);

        Response serviceResponse;
        try {
            serviceResponse = UserService.getInstance().register(registerRequest);
            serialize(serviceResponse, response);
        }
        catch (BadRequestException e) {
            serializeError(e, 400, response);
        }
        catch (AlreadyTakenException e) {
            serializeError(e, 403, response);
        }
        catch (ServerErrorException e) {
            serializeError(e, 500, response);
        }
    }
}