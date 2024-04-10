package webSocketMessages.userCommands;

public class LeaveUserGameCommand extends UserGameCommand {

	public LeaveUserGameCommand(String authToken, Integer gameID) {
		super(authToken, CommandType.LEAVE, gameID);
	}
}
