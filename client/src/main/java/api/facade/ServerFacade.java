package api.facade;

import api.*;
import chess.ChessGame;
import chess.ChessMove;
import client.ServerMessageObserver;
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
	WebsocketCommunicator websocketCommunicator;

	public ServerFacade(ServerMessageObserver observer) throws HTTPConnectionException {
		this(8080, observer);
	}

	public ServerFacade(int port, ServerMessageObserver observer) throws HTTPConnectionException {
		this.port = port;
		this.userHTTPCommunicator = new UserHTTPCommunicator(port);
		this.gameHTTPCommunicator = new GameHTTPCommunicator(port);
		this.websocketCommunicator = new WebsocketCommunicator(observer, port);
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

	public int createGame(String gameName) throws HTTPResponseException, HTTPConnectionException {
		return gameHTTPCommunicator.createGame(new CreateGameRequest(gameName));
	}

	public Set<GameData> listGames() throws HTTPResponseException, HTTPConnectionException {
		ListGamesResponse listGamesResponse = gameHTTPCommunicator.listGames();
		return listGamesResponse.games();
	}

	public void joinGame(String userColor, int gameID) throws HTTPResponseException, HTTPConnectionException {
		gameHTTPCommunicator.joinGame(new JoinGameRequest(userColor, gameID));
		if (userColor == null) {
			websocketCommunicator.joinGameObserver(gameID);
		} else {
			websocketCommunicator.joinGame(gameID, ChessGame.TeamColor.valueOf(userColor));
		}
	}

	public void makeMove(ChessMove move, int gameID) throws HTTPConnectionException {
		websocketCommunicator.makeMove(gameID, move);
	}

	public void leaveGame(int gameID) throws HTTPConnectionException {
		websocketCommunicator.leave(gameID);
	}

	public void resign(int gameID) throws HTTPConnectionException {
		websocketCommunicator.resign(gameID);
	}
}
