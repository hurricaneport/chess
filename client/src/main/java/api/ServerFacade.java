package api;

import chess.ChessGame;
import model.GameData;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;

import java.util.HashSet;
import java.util.Set;

public class ServerFacade {
	int port;
	UserHTTPHandler userHTTPHandler;

	public ServerFacade() {
		this(8080);
	}

	public ServerFacade(int port) {
		this.port = port;
		this.userHTTPHandler = new UserHTTPHandler(port);
	}

	public void login(LoginRequest loginRequest) throws HTTPResponseException, HTTPConnectionException {
		userHTTPHandler.login(loginRequest);
	}

	public void register(RegisterRequest registerRequest) throws HTTPResponseException, HTTPConnectionException {
		userHTTPHandler.register(registerRequest);
	}

	public void logout() throws HTTPResponseException, HTTPConnectionException {
		userHTTPHandler.logout();
	}

	public Set<GameData> listGames() throws HTTPResponseException {
		HashSet<GameData> games = new HashSet<>();
		games.add(new GameData(1, null, null, "game", new ChessGame()));
		games.add(new GameData(2, null, null, "game2", new ChessGame()));
		return games;
	}

	public void joinGame(JoinGameRequest joinGameRequest) throws HTTPResponseException {

	}
}
