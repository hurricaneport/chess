package handler;

import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
	private final Map<Integer, Set<Session>> activeGameSessions = new HashMap<>();
	private final Object lockObject = new Object();
	private final Map<String, Session> activeAuthSessions = new HashMap<>();
	private final Gson gson = GsonFactory.getGson();

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);

		addGameSession(userGameCommand.getGameID(), session);
		addAuthSession(userGameCommand.getAuthString(), session);

		switch (userGameCommand.getCommandType()) {
			case JOIN_PLAYER -> handleJoinPlayer((JoinPlayerUserGameCommand) userGameCommand);
			case JOIN_OBSERVER -> handleJoinObserver((JoinObserverUserGameCommand) userGameCommand);
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

	private void addAuthSession(String authToken, Session session) {
		synchronized (lockObject) {
			activeAuthSessions.put(authToken, session);
		}
	}

	private void handleJoinPlayer(JoinPlayerUserGameCommand joinPlayerUserGameCommand) {

	}

	private void handleJoinObserver(JoinObserverUserGameCommand joinObserverUserGameCommand) {

	}

	private void handleMakeMove(MakeMoveUserGameCommand makeMoveUserGameCommand) {

	}

	private void handleLeave(LeaveUserGameCommand leaveUserGameCommand) {

	}

	private void handleResign(ResignUserGameCommand resignUserGameCommand) {

	}

	private void sendNotification(Session session, String message) {
		try {
			NotificationServerMessage notificationServerMessage = new NotificationServerMessage(message);
			session.getRemote().sendString(gson.toJson(notificationServerMessage));
		} catch (IOException e) {
			
		}
	}
}
