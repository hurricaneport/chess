package handler;

import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
	Gson gson = GsonFactory.getGson();

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
		switch (userGameCommand.getCommandType()) {
			case JOIN_PLAYER -> {
			}
			case JOIN_OBSERVER -> {
			}
			case MAKE_MOVE -> {
			}
			case LEAVE -> {
			}
			case RESIGN -> {
			}
		}
	}
}
