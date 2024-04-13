package passoffTests.typeAdapterTests;

import chess.ChessGame;
import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.LoadGameServerMessage;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageTests {
	private final Gson gson = GsonFactory.getGson();

	@Test
	@DisplayName("Test Error Message")
	public void errorMessage() {
		ServerMessage serverMessage = new ErrorServerMessage("message");
		String json = gson.toJson(serverMessage);

		ServerMessage serverMessage1 = gson.fromJson(json, ServerMessage.class);

		Assertions.assertEquals(ErrorServerMessage.class, serverMessage1.getClass());
	}

	@Test
	@DisplayName("Test Notification Message")
	public void notificationMessage() {
		ServerMessage serverMessage = new NotificationServerMessage("message");
		String json = gson.toJson(serverMessage);

		ServerMessage serverMessage1 = gson.fromJson(json, ServerMessage.class);

		Assertions.assertEquals(NotificationServerMessage.class, serverMessage1.getClass());
	}

	@Test
	@DisplayName("Test Load Game Message")
	public void loadGameMessage() {
		ServerMessage serverMessage = new LoadGameServerMessage(new ChessGame());
		String json = gson.toJson(serverMessage);

		ServerMessage serverMessage1 = gson.fromJson(json, ServerMessage.class);

		Assertions.assertEquals(LoadGameServerMessage.class, serverMessage1.getClass());
	}

}
