package webSocketMessages.userCommands;

public class LeaveUserGameCommand extends UserGameCommand {
	public LeaveUserGameCommand(String authToken) {
		super(authToken, CommandType.LEAVE);
	}
}
