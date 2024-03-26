package api;

import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.ListGamesResponse;

import java.util.Set;

public class ServerFacade {
	int port;
	UserHTTPCommunicator userHTTPCommunicator;
	GameHTTPCommunicator gameHTTPCommunicator;

	public ServerFacade() {
		this(8080);
	}

	public ServerFacade(int port) {
		this.port = port;
		this.userHTTPCommunicator = new UserHTTPCommunicator(port);
		gameHTTPCommunicator = new GameHTTPCommunicator(port);
	}

	public void login(String username, String password) throws HTTPResponseException, HTTPConnectionException {
		userHTTPCommunicator.login(new LoginRequest(username, password));
	}

	public void register(String username, String password, String email) throws HTTPResponseException, HTTPConnectionException {
		userHTTPCommunicator.register(new RegisterRequest(username, password, email));
	}

	public void logout() throws HTTPResponseException, HTTPConnectionException {
		userHTTPCommunicator.logout();
	}

	public void createGame(String gameName) throws HTTPResponseException, HTTPConnectionException {
		gameHTTPCommunicator.createGame(new CreateGameRequest(gameName));
	}

	public Set<GameData> listGames() throws HTTPResponseException, HTTPConnectionException {
		ListGamesResponse listGamesResponse = gameHTTPCommunicator.listGames();
		return listGamesResponse.games();
	}

	public void joinGame(String userColor, int gameID) throws HTTPResponseException, HTTPConnectionException {
		gameHTTPCommunicator.joinGame(new JoinGameRequest(userColor, gameID));
	}
}
