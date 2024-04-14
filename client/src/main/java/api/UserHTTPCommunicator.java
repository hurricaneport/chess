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
	ConnectionManager connectionManager;

	public UserHTTPCommunicator(int port) {
		this.port = port;
		connectionManager = new ConnectionManager(port);
	}

	public void login(LoginRequest loginRequest) throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		LoginResponse loginResponse;

		try {
			HttpURLConnection connection = connectionManager.getConnection("/session",
					"POST", headers, true);

			connectionManager.writeRequestBody(loginRequest, connection);

			if (ConnectionManager.httpStatusIsOkay(connection)) {
				loginResponse = connectionManager.readResponseBody(LoginResponse.class, connection);
			} else {
				ErrorResponse errorResponse = connectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}

			ConnectionManager.updateAuthToken(loginResponse.authToken());
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
			connection = connectionManager.getConnection("/user",
					"POST", headers, true);

			connectionManager.writeRequestBody(registerRequest, connection);

			if (ConnectionManager.httpStatusIsOkay(connection)) {
				registerResponse = connectionManager.readResponseBody(RegisterResponse.class, connection);
			} else {
				ErrorResponse errorResponse = connectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}

			ConnectionManager.updateAuthToken(registerResponse.authToken());
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

	public void logout() throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", ConnectionManager.getAuthToken());

		try {
			HttpURLConnection connection = connectionManager.getConnection("/session", "DELETE", headers, false);
			if (ConnectionManager.httpStatusIsOkay(connection)) {
				ConnectionManager.clearAuthToken();
			} else {
				ErrorResponse errorResponse = connectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

}
