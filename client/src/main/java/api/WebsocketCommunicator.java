package api;

import chess.ChessGame;
import chess.ChessMove;
import client.ServerMessageObserver;
import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
	Session session;
	ServerMessageObserver observer;
	Gson gson = GsonFactory.getGson();
	String webSocketUri;

	public WebsocketCommunicator(ServerMessageObserver observer, int port) throws HTTPConnectionException {
		this.observer = observer;
		webSocketUri = "ws://localhost:" + port + "/connect";
		session = getWebSocketSession();
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
				observer.notify(serverMessage);
			}
		});

	}

	public Session getWebSocketSession() throws HTTPConnectionException {
		URI uri;
		try {
			uri = new URI(webSocketUri);
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			return container.connectToServer(this, ClientEndpointConfig.Builder.create().build(), uri);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (DeploymentException | IOException e) {
			throw new HTTPConnectionException("Could not establish websocket connection");
		}

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

	public void leave(int gameID) throws HTTPConnectionException {
		try {
			LeaveUserGameCommand leaveUserGameCommand = new LeaveUserGameCommand(
					ConnectionManager.getAuthToken(), gameID);
			session.getBasicRemote().sendText(gson.toJson(leaveUserGameCommand));
		} catch (IOException e) {
			throw new HTTPConnectionException("Could not send Websocket request");
		}
	}

	public void resign(int gameID) throws HTTPConnectionException {
		try {
			ResignUserGameCommand resignUserGameCommand = new ResignUserGameCommand(
					ConnectionManager.getAuthToken(), gameID);
			session.getBasicRemote().sendText(gson.toJson(resignUserGameCommand));
		} catch (IOException e) {
			throw new HTTPConnectionException("Could not send websocket request");
		}
	}
}
