package webSocketMessages.userCommands;

public class LeaveUserGameCommand extends UserGameCommand {
	Integer gameID;

	public LeaveUserGameCommand(String authToken, Integer gameID) {
		super(authToken, CommandType.LEAVE);
		this.gameID = gameID;
	}

	public Integer getGameID() {
		return gameID;
	}
}
