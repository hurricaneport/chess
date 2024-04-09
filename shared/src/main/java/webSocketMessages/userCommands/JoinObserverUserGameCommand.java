package webSocketMessages.userCommands;

public class JoinObserverUserGameCommand extends UserGameCommand {
	Integer gameID;

	public JoinObserverUserGameCommand(String authToken, Integer gameID) {
		super(authToken, CommandType.JOIN_OBSERVER);
		this.gameID = gameID;
	}

	public Integer getGameID() {
		return gameID;
	}
}
