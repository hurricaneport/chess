package webSocketMessages.userCommands;

public class ResignUserGameCommand extends UserGameCommand {
	Integer gameID;

	public ResignUserGameCommand(String authToken, Integer gameID) {
		super(authToken, CommandType.RESIGN);
		this.gameID = gameID;
	}
}
