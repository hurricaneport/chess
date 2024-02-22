package server.handler;

import com.google.gson.Gson;
import server.ErrorResponse;
import server.RegisterRequest;
import server.RegisterResponse;
import server.Response;
import service.UserService;

public class RegisterHandler extends Handler{
    public void handleRegistration(spark.Request request, spark.Response response) {
        System.out.println("Created new Register Handler");

        RegisterRequest registerRequest = deserialize(request, RegisterRequest.class);

        Response serviceResponse = UserService.getInstance().register(registerRequest);

        serializeResponse(serviceResponse, response, RegisterResponse.class);

    }
}
