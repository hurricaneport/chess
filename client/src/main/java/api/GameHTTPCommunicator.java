package api;

import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResponse;
import model.response.ErrorResponse;
import model.response.ListGamesResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class GameHTTPCommunicator {
	int port;

	GameHTTPCommunicator(int port) {
		this.port = port;
		httpConnectionManager = new HTTPConnectionManager(port);
	}

	HTTPConnectionManager httpConnectionManager;

	public void createGame(CreateGameRequest createGameRequest) throws HTTPResponseException, HTTPConnectionException {
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
	}

	public void joinGame(JoinGameRequest joinGameRequest) throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", HTTPConnectionManager.getAuthToken());

		try {
			HttpURLConnection connection = httpConnectionManager.getConnection("/game", "PUT", headers, true);

			httpConnectionManager.writeRequestBody(joinGameRequest, connection);

			if (connection.getResponseCode() != 200) {
				ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

	public ListGamesResponse listGames() throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", HTTPConnectionManager.getAuthToken());

		ListGamesResponse listGamesResponse;
		try {
			HttpURLConnection connection = httpConnectionManager.getConnection("/game", "GET", headers, false);

			if (connection.getResponseCode() == 200) {
				listGamesResponse = httpConnectionManager.readResponseBody(ListGamesResponse.class, connection);
			} else {
				ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
		return listGamesResponse;
	}
}
