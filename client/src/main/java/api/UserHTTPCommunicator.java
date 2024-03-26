package api;

import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.ErrorResponse;
import model.response.LoginResponse;
import model.response.RegisterResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class UserHTTPCommunicator {
	int port;
	HTTPConnectionManager httpConnectionManager;

	UserHTTPCommunicator(int port) {
		this.port = port;
		httpConnectionManager = new HTTPConnectionManager(port);
	}

	public void login(LoginRequest loginRequest) throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		LoginResponse loginResponse;

		try {
			HttpURLConnection connection = httpConnectionManager.getConnection("/session",
					"POST", headers, true);

			httpConnectionManager.writeRequestBody(loginRequest, connection);

			if (HTTPConnectionManager.httpStatusIsOkay(connection)) {
				loginResponse = httpConnectionManager.readResponseBody(LoginResponse.class, connection);
			} else {
				ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}

			HTTPConnectionManager.updateAuthToken(loginResponse.authToken());
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

	public void register(RegisterRequest registerRequest) throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		RegisterResponse registerResponse;
		HttpURLConnection connection;
		try {
			connection = httpConnectionManager.getConnection("/user",
					"POST", headers, true);

			httpConnectionManager.writeRequestBody(registerRequest, connection);

			if (HTTPConnectionManager.httpStatusIsOkay(connection)) {
				registerResponse = httpConnectionManager.readResponseBody(RegisterResponse.class, connection);
			} else {
				ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}

			HTTPConnectionManager.updateAuthToken(registerResponse.authToken());
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

	public void logout() throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", HTTPConnectionManager.getAuthToken());

		try {
			HttpURLConnection connection = httpConnectionManager.getConnection("/session", "DELETE", headers, false);
			if (HTTPConnectionManager.httpStatusIsOkay(connection)) {
				HTTPConnectionManager.clearAuthToken();
			} else {
				ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

}
