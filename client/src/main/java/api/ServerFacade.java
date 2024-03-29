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
	UserHTTPHandler userHTTPHandler;
	GameHTTPHandler gameHTTPHandler;

	public ServerFacade() {
		this(8080);
	}

	public ServerFacade(int port) {
		this.port = port;
		this.userHTTPHandler = new UserHTTPHandler(port);
		gameHTTPHandler = new GameHTTPHandler(port);
	}

	public void login(String username, String password) throws HTTPResponseException, HTTPConnectionException {
		userHTTPHandler.login(new LoginRequest(username, password));
	}

	public void register(String username, String password, String email) throws HTTPResponseException, HTTPConnectionException {
		userHTTPHandler.register(new RegisterRequest(username, password, email));
	}

	public void logout() throws HTTPResponseException, HTTPConnectionException {
		userHTTPHandler.logout();
	}

	public void createGame(String gameName) throws HTTPResponseException, HTTPConnectionException {
		gameHTTPHandler.createGame(new CreateGameRequest(gameName));
	}

	public Set<GameData> listGames() throws HTTPResponseException, HTTPConnectionException {
		ListGamesResponse listGamesResponse = gameHTTPHandler.listGames();
		return listGamesResponse.games();
	}

	public void joinGame(String userColor, int gameID) throws HTTPResponseException, HTTPConnectionException {
		gameHTTPHandler.joinGame(new JoinGameRequest(userColor, gameID));
	}
}
