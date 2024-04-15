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
import java.util.*;

@WebSocket
public class WebSocketHandler {
	private final Map<Integer, Set<Session>> activeGameSessions = new HashMap<>();
	private final Object lockObject = new Object();
	private final Gson gson = GsonFactory.getGson();

	private final GameDAO gameDAO = DBGameDAO.getGameDAO();

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {

	}

	@OnWebSocketConnect
	public void onConnect(Session session) {

	}

	@OnWebSocketError
	public void onError(Throwable throwable) {

	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws IOException, ServerErrorException, DataAccessException {
		UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
		AuthData authData = (new GameService()).authorize(userGameCommand.getAuthString());

		if (authData == null) {
			sendError(session, "Error: Unauthorized. Please log in again.");
			return;
		}

		GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
		if (gameData == null) {
			sendError(session, "Error: Invalid Game ID.");
			return;
		}

		addGameSession(userGameCommand.getGameID(), session);

		String username = authData.username();

		ChessGame.TeamColor playerColor = findPlayerColor(username, userGameCommand.getGameID());

		switch (userGameCommand.getCommandType()) {
			case JOIN_PLAYER -> handleJoinPlayer((JoinPlayerUserGameCommand) userGameCommand, session, username);
			case JOIN_OBSERVER -> handleJoinObserver((JoinObserverUserGameCommand) userGameCommand, session, username);
			case MAKE_MOVE -> handleMakeMove((MakeMoveUserGameCommand) userGameCommand, session, username, playerColor);
			case LEAVE -> handleLeave((LeaveUserGameCommand) userGameCommand, session, username);
			case RESIGN -> handleResign((ResignUserGameCommand) userGameCommand, session, username, playerColor);
		}
	}

	private ChessGame.TeamColor findPlayerColor(String username, Integer gameID) throws DataAccessException {
		GameData gameData = gameDAO.getGame(gameID);

		if (Objects.equals(gameData.whiteUsername(), username)) {
			return ChessGame.TeamColor.WHITE;
		} else if (Objects.equals(gameData.blackUsername(), username)) {
			return ChessGame.TeamColor.BLACK;
		} else {
			return null;
		}
	}

	private void addGameSession(Integer gameID, Session session) {
		synchronized (lockObject) {
			activeGameSessions.computeIfAbsent(gameID, k -> new HashSet<>());
			activeGameSessions.get(gameID).add(session);
		}
	}

	private void handleJoinPlayer(JoinPlayerUserGameCommand joinPlayerUserGameCommand, Session session, String username) throws IOException, DataAccessException {
		GameData gameData = gameDAO.getGame(joinPlayerUserGameCommand.getGameID());
		ChessGame game = gameData.game();

		String gameDataUsername = joinPlayerUserGameCommand.getPlayerColor() == ChessGame.TeamColor.BLACK ? gameData.blackUsername() : gameData.whiteUsername();

		if (!Objects.equals(gameDataUsername, username)) {
			sendError(session, "Error: You have not been added as a player to the game. HTTP Join Game request must be sent first.");
			return;
		}

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

	private void handleMakeMove(MakeMoveUserGameCommand makeMoveUserGameCommand, Session session, String username, ChessGame.TeamColor playerColor) throws DataAccessException, IOException {
		if (playerColor == null) {
			sendError(session, "Error: Cannot make moves as an observer");
			return;
		}

		GameData gameData = gameDAO.getGame(makeMoveUserGameCommand.getGameID());
		ChessGame game = gameData.game();

		if (game.isGameIsOver()) {
			sendError(session, "Error: Game is over.");
			return;
		}

		if (game.getTeamTurn() != playerColor) {
			sendError(session, "Error: Not your turn.");
			return;
		}

		ChessGame.TeamColor opponentColor = playerColor == ChessGame.TeamColor.BLACK ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
		String opponentUsername = playerColor == ChessGame.TeamColor.BLACK ? gameData.whiteUsername() : gameData.blackUsername();

		try {
			game.makeMove(makeMoveUserGameCommand.getChessMove());
		} catch (InvalidMoveException e) {
			sendError(session, "Error: Chess move invalid: " + e);
			return;
		}

		if (game.isInCheck(opponentColor)) {
			for (Session session1 : activeGameSessions.get(makeMoveUserGameCommand.getGameID())) {
				sendNotification(session1, opponentUsername + " is in check and must move out of check");
			}
		} else if (game.isInCheckmate(opponentColor)) {
			for (Session session1 : activeGameSessions.get(makeMoveUserGameCommand.getGameID())) {
				sendNotification(session1, opponentUsername + " is in checkmate and the game is over. " + playerColor + " wins!");
			}
			game.endGame();
		} else if (game.isInStalemate(opponentColor)) {
			for (Session session1 : activeGameSessions.get(makeMoveUserGameCommand.getGameID())) {
				sendNotification(session1, opponentUsername + " is in stalemate. The game has ended in a draw.");
			}
			game.endGame();
		}

		try {
			gameDAO.updateGame(gameData.gameID(), gameData);
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

	private void handleResign(ResignUserGameCommand resignUserGameCommand, Session session, String username, ChessGame.TeamColor playerColor) throws DataAccessException, IOException {
		if (playerColor == null) {
			sendError(session, "Error: Cannot resign as an observer.");
			return;
		}

		ChessGame.TeamColor opponentColor = playerColor == ChessGame.TeamColor.BLACK ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

		GameData gameData = gameDAO.getGame(resignUserGameCommand.getGameID());

		if (gameData.game().isGameIsOver()) {
			sendError(session, "Error: Can't resign from a game that is over.");
			return;
		}

		gameData.game().endGame();
		gameDAO.updateGame(gameData.gameID(), gameData);

		for (Session session1 : activeGameSessions.get(gameData.gameID())) {
			sendNotification(session1, username + "has resigned. " + opponentColor + " wins!");
		}
	}

	private void sendNotification(Session session, String message) throws IOException {
		if (session.isOpen()) {
			NotificationServerMessage notificationServerMessage = new NotificationServerMessage(message);
			session.getRemote().sendString(gson.toJson(notificationServerMessage));
		}
	}

	private void sendError(Session session, String errorMessage) throws IOException {
		if (session.isOpen()) {
			ErrorServerMessage errorServerMessage = new ErrorServerMessage(errorMessage);
			session.getRemote().sendString(gson.toJson(errorServerMessage));
		}
	}

	private void updateGame(Session session, ChessGame game) throws IOException {
		if (session.isOpen()) {
			LoadGameServerMessage loadGameServerMessage = new LoadGameServerMessage(game);
			session.getRemote().sendString(gson.toJson(loadGameServerMessage));
		}
	}
}