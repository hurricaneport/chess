package api;

import chess.ChessGame;
import chess.ChessMove;
import client.ServerMessageObserver;
import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverUserGameCommand;
import webSocketMessages.userCommands.JoinPlayerUserGameCommand;
import webSocketMessages.userCommands.MakeMoveUserGameCommand;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;

public class WebsocketCommunicator extends Endpoint {
	Session session;
	ServerMessageObserver observer;
	ConnectionManager connectionManager;
	Gson gson = GsonFactory.getGson();

	public WebsocketCommunicator(ServerMessageObserver observer, int port) throws HTTPConnectionException {
		this.observer = observer;
		connectionManager = new ConnectionManager(port);
		session = connectionManager.getWebSocketSession(this);

		session.addMessageHandler((MessageHandler.Whole<String>) message -> {
			ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
			observer.notify(serverMessage);
		});
	}

	@Override
	public void onOpen(Session session, EndpointConfig endpointConfig) {

	}

	public void joinGame(int gameID, ChessGame.TeamColor playerColor) throws HTTPConnectionException {
		try {
			JoinPlayerUserGameCommand joinPlayerUserGameCommand = new JoinPlayerUserGameCommand(
					ConnectionManager.getAuthToken(), gameID, playerColor);

			session.getBasicRemote().sendText(gson.toJson(joinPlayerUserGameCommand));
		} catch (IOException e) {
			throw new HTTPConnectionException("Could not send Websocket request");
		}
	}

	public void joinGameObserver(int gameID) throws HTTPConnectionException {
		try {
			JoinObserverUserGameCommand joinObserverUserGameCommand = new JoinObserverUserGameCommand(
					ConnectionManager.getAuthToken(), gameID);

			session.getBasicRemote().sendText(gson.toJson(joinObserverUserGameCommand));
		} catch (IOException e) {
			throw new HTTPConnectionException("Could not send Websocket request");
		}
	}

	public void makeMove(int gameID, ChessMove chessMove) throws HTTPConnectionException {
		try {
			MakeMoveUserGameCommand makeMoveUserGameCommand = new MakeMoveUserGameCommand(
					ConnectionManager.getAuthToken(), gameID, chessMove);

			session.getBasicRemote().sendText(gson.toJson(makeMoveUserGameCommand));
		} catch (IOException e) {
			throw new HTTPConnectionException("Could not send Websocket request");
		}
	}

}
