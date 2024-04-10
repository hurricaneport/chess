package webSocketMessages.userCommands;

public class ResignUserGameCommand extends UserGameCommand {

	public ResignUserGameCommand(String authToken, Integer gameID) {
		super(authToken, CommandType.RESIGN, gameID);
	}
}
