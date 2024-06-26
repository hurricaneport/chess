package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {
	protected CommandType commandType;
	private final String authToken;
	private final Integer gameID;

	public UserGameCommand(String authToken, CommandType type, Integer gameID) {
		this.authToken = authToken;
		this.commandType = type;
		this.gameID = gameID;
	}

	public enum CommandType {
		JOIN_PLAYER,
		JOIN_OBSERVER,
		MAKE_MOVE,
		LEAVE,
		RESIGN
	}

	public String getAuthString() {
		return authToken;
	}

	public CommandType getCommandType() {
		return this.commandType;
	}

	public Integer getGameID() {
		return gameID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UserGameCommand that))
			return false;
		return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCommandType(), getAuthString());
	}
}
