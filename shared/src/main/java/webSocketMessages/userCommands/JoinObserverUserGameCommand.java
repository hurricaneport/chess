package webSocketMessages.userCommands;

public class JoinObserverUserGameCommand extends UserGameCommand {

	public JoinObserverUserGameCommand(String authToken, Integer gameID) {
		super(authToken, CommandType.JOIN_OBSERVER, gameID);
	}
}
