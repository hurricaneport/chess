package webSocketMessages.userCommands;

public class MakeMoveUserGameCommand extends UserGameCommand {
	public MakeMoveUserGameCommand(String authToken) {
		super(authToken, CommandType.MAKE_MOVE);
	}
}
