package api;

import model.request.CreateGameRequest;
import model.response.CreateGameResponse;
import model.response.ErrorResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class GameHTTPHandler {
	int port;

	GameHTTPHandler(int port) {
		this.port = port;
	}

	HTTPConnectionManager httpConnectionManager = new HTTPConnectionManager(port);

	public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", HTTPConnectionManager.getAuthToken());

		CreateGameResponse createGameResponse;

		try {
			HttpURLConnection connection = httpConnectionManager.getConnection("/game", "POST", headers, true);
			httpConnectionManager.writeRequestBody(createGameRequest, connection);

			if (connection.getResponseCode() == 200) {
				createGameResponse = httpConnectionManager.readResponseBody(CreateGameResponse.class, connection);
			} else {
				ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
		return createGameResponse;
	}
}
