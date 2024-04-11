package webSocketMessages.serverMessages;

public class NotificationServerMessage extends ServerMessage {
	String message;

	public NotificationServerMessage(String message) {
		super(ServerMessageType.NOTIFICATION);
		this.message = message;
	}
}
