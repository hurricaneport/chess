package handler;

import model.request.LoginRequest;
import model.response.Response;
import service.UserService;
import service.exceptions.BadRequestException;
import service.exceptions.ServerErrorException;
import service.exceptions.UnauthorizedException;

public class LoginHandler extends Handler {
	public void handleLogin(spark.Request request, spark.Response response) {
		System.out.println("Created new Login Handler");

		LoginRequest serviceRequest = deserialize(request, LoginRequest.class);

		Response serviceResponse;
		try {
			serviceResponse = UserService.getInstance().login(serviceRequest);
			serialize(serviceResponse, response);
		} catch (BadRequestException e) {
			serializeError(e, 400, response);
		} catch (UnauthorizedException e) {
			serializeError(e, 401, response);
		} catch (ServerErrorException e) {
			serializeError(e, 500, response);
		}
	}
}
