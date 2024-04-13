package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.GameDAO;
import dataAccess.sql.DBGameDAO;
import jsonUtils.GsonFactory;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.exceptions.ServerErrorException;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.LoadGameServerMessage;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.*;

@WebSocket
public class WebSocketHandler {
	private final Map<Integer, Set<Session>> activeGameSessions = new HashMap<>();
	private final Object lockObject = new Object();
	private final Map<Session, String> activeUserSessions = new HashMap<>();
	private final Gson gson = GsonFactory.getGson();

	private final GameDAO gameDAO = DBGameDAO.getGameDAO();

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws IOException, ServerErrorException {
		UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);

		addGameSession(userGameCommand.getGameID(), session);
		AuthData authData = (new GameService()).authorize(userGameCommand.getAuthString());
		String username = authData.username();
		addSessionUsername(authData.username(), session);

		switch (userGameCommand.getCommandType()) {
			case JOIN_PLAYER -> handleJoinPlayer((JoinPlayerUserGameCommand) userGameCommand, username);
			case JOIN_OBSERVER -> handleJoinObserver((JoinObserverUserGameCommand) userGameCommand, username);
			case MAKE_MOVE -> handleMakeMove((MakeMoveUserGameCommand) userGameCommand);
			case LEAVE -> handleLeave((LeaveUserGameCommand) userGameCommand);
			case RESIGN -> handleResign((ResignUserGameCommand) userGameCommand);
		}
	}

	private void addGameSession(Integer gameID, Session session) {
		synchronized (lockObject) {
			activeGameSessions.computeIfAbsent(gameID, k -> new HashSet<>());
			activeGameSessions.get(gameID).add(session);
		}
	}

	private void addSessionUsername(String username, Session session) {
		synchronized (lockObject) {
			activeUserSessions.put(session, username);
		}
	}

	private void handleJoinPlayer(JoinPlayerUserGameCommand joinPlayerUserGameCommand, String username) throws IOException {
		String message = username + " has joined the game as " + joinPlayerUserGameCommand.getPlayerColor() + "\n";
		for (Session session : activeGameSessions.get(joinPlayerUserGameCommand.getGameID())) {
			if (!Objects.equals(activeUserSessions.get(session), username)) {
				sendNotification(session, message);
			}
		}
	}

	private void handleJoinObserver(JoinObserverUserGameCommand joinObserverUserGameCommand, String username) throws IOException {
		String message = username + " has joined the game as an observer\n";
		for (Session session : activeGameSessions.get(joinObserverUserGameCommand.getGameID())) {
			if (!Objects.equals(activeUserSessions.get(session), username)) {
				sendNotification(session, message);
			}
		}
	}

	private void handleMakeMove(MakeMoveUserGameCommand makeMoveUserGameCommand) {
		
	}

	private void handleLeave(LeaveUserGameCommand leaveUserGameCommand) {

	}

	private void handleResign(ResignUserGameCommand resignUserGameCommand) {

	}

	private void sendNotification(Session session, String message) throws IOException {
		NotificationServerMessage notificationServerMessage = new NotificationServerMessage(message);
		session.getRemote().sendString(gson.toJson(notificationServerMessage));
	}

	private void sendError(Session session, String errorMessage) throws IOException {
		ErrorServerMessage errorServerMessage = new ErrorServerMessage(errorMessage);
		session.getRemote().sendString(gson.toJson(errorServerMessage));
	}

	private void updateGame(Session session, ChessGame game) throws IOException {
		LoadGameServerMessage loadGameServerMessage = new LoadGameServerMessage(game);
		session.getRemote().sendString(gson.toJson(loadGameServerMessage));
	}
}
