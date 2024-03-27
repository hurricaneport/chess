package webSocketMessages.userCommands;

public class JoinPlayerUserGameCommand extends UserGameCommand {
	public JoinPlayerUserGameCommand(String authToken) {
		super(authToken, CommandType.JOIN_PLAYER);
	}
}
