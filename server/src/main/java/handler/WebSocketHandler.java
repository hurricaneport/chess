package handler;

import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;

@WebSocket
public class WebSocketHandler {
	Gson gson = GsonFactory.getGson();

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
		switch (userGameCommand.getCommandType()) {
			case JOIN_PLAYER -> handleJoinPlayer((JoinPlayerUserGameCommand) userGameCommand);
			case JOIN_OBSERVER -> handleJoinObserver((JoinObserverUserGameCommand) userGameCommand);
			case MAKE_MOVE -> handleMakeMove((MakeMoveUserGameCommand) userGameCommand);
			case LEAVE -> handleLeave((LeaveUserGameCommand) userGameCommand);
			case RESIGN -> handleResign((ResignUserGameCommand) userGameCommand);
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
}
