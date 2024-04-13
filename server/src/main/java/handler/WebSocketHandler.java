package handler;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.sql.DBGameDAO;
import jsonUtils.GsonFactory;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.GameService;
import service.exceptions.ServerErrorException;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.LoadGameServerMessage;
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
	private final Map<Session, String> activeUserSessions = new HashMap<>();
	private final Gson gson = GsonFactory.getGson();

	private final GameDAO gameDAO = DBGameDAO.getGameDAO();

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		synchronized (lockObject) {
			activeUserSessions.remove(session);
			for (Integer gameID : activeGameSessions.keySet()) {
				for (Session session1 : activeGameSessions.get(gameID)) {
					if (session1.equals(session)) {
						activeGameSessions.get(gameID).remove(session);
					}
				}
			}
		}
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
	}

	@OnWebSocketError
	public void onError(Throwable throwable) {
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws IOException, ServerErrorException, DataAccessException {
		System.out.print("onMessage");
		UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
		AuthData authData = (new GameService()).authorize(userGameCommand.getAuthString());

		if (authData == null) {
			sendError(session, "Error: Unauthorized. Please log in again.");
			return;
		}

		addGameSession(userGameCommand.getGameID(), session);

		String username = authData.username();
		addSessionUsername(authData.username(), session);

		switch (userGameCommand.getCommandType()) {
			case JOIN_PLAYER -> handleJoinPlayer((JoinPlayerUserGameCommand) userGameCommand, session, username);
			case JOIN_OBSERVER -> handleJoinObserver((JoinObserverUserGameCommand) userGameCommand, session, username);
			case MAKE_MOVE -> handleMakeMove((MakeMoveUserGameCommand) userGameCommand, session, username);
			case LEAVE -> handleLeave((LeaveUserGameCommand) userGameCommand, session, username);
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

	private void handleJoinPlayer(JoinPlayerUserGameCommand joinPlayerUserGameCommand, Session session, String username) throws IOException, DataAccessException {
		GameData gameData = gameDAO.getGame(joinPlayerUserGameCommand.getGameID());
		ChessGame game = gameData.game();

		updateGame(session, game);

		String message = username + " has joined the game as " + joinPlayerUserGameCommand.getPlayerColor();
		for (Session session1 : activeGameSessions.get(joinPlayerUserGameCommand.getGameID())) {
			if (!session1.equals(session)) {
				sendNotification(session1, message);
			}
		}
	}

	private void handleJoinObserver(JoinObserverUserGameCommand joinObserverUserGameCommand, Session session, String username) throws IOException, DataAccessException {
		GameData gameData = gameDAO.getGame(joinObserverUserGameCommand.getGameID());
		ChessGame game = gameData.game();

		updateGame(session, game);

		String message = username + " has joined the game as an observer";
		for (Session session1 : activeGameSessions.get(joinObserverUserGameCommand.getGameID())) {
			if (!session1.equals(session)) {
				sendNotification(session1, message);
			}
		}
	}

	private void handleMakeMove(MakeMoveUserGameCommand makeMoveUserGameCommand, Session session, String username) throws DataAccessException, IOException {
		GameData gameData = gameDAO.getGame(makeMoveUserGameCommand.getGameID());
		ChessGame game = gameData.game();
		try {
			game.makeMove(makeMoveUserGameCommand.getChessMove());
		} catch (InvalidMoveException e) {
			sendError(session, "Chess move invalid");
			return;
		}
		GameData gameData1 = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
		try {
			gameDAO.updateGame(gameData1.gameID(), gameData1);
		} catch (DataAccessException e) {
			sendError(session, "Move could not be made due to error, please try again");
		}

		for (Session session1 : activeGameSessions.get(makeMoveUserGameCommand.getGameID())) {
			updateGame(session1, game);
			if (!session1.equals(session)) {
				sendNotification(session1, username + "made the move: " + makeMoveUserGameCommand.getChessMove());
			}
		}

	}

	private void handleLeave(LeaveUserGameCommand leaveUserGameCommand, Session session, String username) throws DataAccessException, IOException {
		GameData gameData = gameDAO.getGame(leaveUserGameCommand.getGameID());
		String newWhiteUsername = gameData.whiteUsername();
		String newBlackUsername = gameData.blackUsername();
		if (username.equals(gameData.whiteUsername())) {
			newWhiteUsername = null;
		} else if (username.equals(gameData.blackUsername())) {
			newBlackUsername = null;
		}
		GameData gameData1 = new GameData(gameData.gameID(), newWhiteUsername, newBlackUsername, gameData.gameName(), gameData.game());

		gameDAO.updateGame(gameData1.gameID(), gameData1);

		activeGameSessions.get(leaveUserGameCommand.getGameID()).remove(session);

		for (Session session1 : activeGameSessions.get(leaveUserGameCommand.getGameID())) {
			sendNotification(session1, username + " has left the game");
		}
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