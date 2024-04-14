package webSocketMessages.serverMessages;

public class NotificationServerMessage extends ServerMessage {
	private final String message;

	public NotificationServerMessage(String message) {
		super(ServerMessageType.NOTIFICATION);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
