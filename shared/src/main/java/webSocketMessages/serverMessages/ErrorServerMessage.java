package webSocketMessages.serverMessages;

public class ErrorServerMessage extends ServerMessage {
	public ErrorServerMessage() {
		super(ServerMessageType.ERROR);
	}
}
