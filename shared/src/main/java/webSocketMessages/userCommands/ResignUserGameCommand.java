package webSocketMessages.userCommands;

public class ResignUserGameCommand extends UserGameCommand {
	public ResignUserGameCommand(String authToken) {
		super(authToken, CommandType.RESIGN);
	}
}
