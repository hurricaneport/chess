package webSocketMessages.userCommands;

public class JoinObserverUserGameCommand extends UserGameCommand {
	public JoinObserverUserGameCommand(String authToken) {
		super(authToken, CommandType.JOIN_OBSERVER);
	}
}
