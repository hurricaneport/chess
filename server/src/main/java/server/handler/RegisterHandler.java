package server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.security.LoginService;
import server.ErrorResponse;
import server.RegisterRequest;
import server.RegisterResponse;
import server.Response;
import service.UserService;

public class RegisterHandler {
    public void handleRegistration(spark.Request request, spark.Response response) {
        System.out.println("Created new Register Handler");
        Gson serializer = new Gson();

        RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);

        Response serviceResponse = UserService.getInstance().register(registerRequest);

        String responseString = serializer.toJson(serviceResponse);

        if (serviceResponse.getClass() == RegisterResponse.class) {
            response.status(200);
        }
        else if(serviceResponse.getClass() == ErrorResponse.class) {
            response.status(((ErrorResponse) serviceResponse).status());
            ErrorResponse errorResponse = new ErrorResponse(((ErrorResponse) serviceResponse).message(), null);
            responseString = serializer.toJson(errorResponse);
        }

        response.body(responseString);
    }
}
