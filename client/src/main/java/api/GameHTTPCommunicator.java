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
		connectionManager = new ConnectionManager(port);
	}

	ConnectionManager connectionManager;

	public void createGame(CreateGameRequest createGameRequest) throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", ConnectionManager.getAuthToken());

		CreateGameResponse createGameResponse;

		try {
			HttpURLConnection connection = connectionManager.getConnection("/game", "POST", headers, true);
			connectionManager.writeRequestBody(createGameRequest, connection);

			if (connection.getResponseCode() == 200) {
				createGameResponse = connectionManager.readResponseBody(CreateGameResponse.class, connection);
			} else {
				ErrorResponse errorResponse = connectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

	public void joinGame(JoinGameRequest joinGameRequest) throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", ConnectionManager.getAuthToken());

		try {
			HttpURLConnection connection = connectionManager.getConnection("/game", "PUT", headers, true);

			connectionManager.writeRequestBody(joinGameRequest, connection);

			if (connection.getResponseCode() != 200) {
				ErrorResponse errorResponse = connectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
	}

	public ListGamesResponse listGames() throws HTTPResponseException, HTTPConnectionException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("authorization", ConnectionManager.getAuthToken());

		ListGamesResponse listGamesResponse;
		try {
			HttpURLConnection connection = connectionManager.getConnection("/game", "GET", headers, false);

			if (connection.getResponseCode() == 200) {
				listGamesResponse = connectionManager.readResponseBody(ListGamesResponse.class, connection);
			} else {
				ErrorResponse errorResponse = connectionManager.readErrorBody(connection);
				throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
			}
		} catch (IOException e) {
			throw new HTTPConnectionException(e.getMessage());
		}
		return listGamesResponse;
	}
}
