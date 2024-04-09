package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerUserGameCommand extends UserGameCommand {
	private final Integer gameID;
	private final ChessGame.TeamColor playerColor;

	public JoinPlayerUserGameCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
		super(authToken, CommandType.JOIN_PLAYER);
		this.gameID = gameID;
		this.playerColor = playerColor;
	}

	public Integer getGameID() {
		return gameID;
	}

	public ChessGame.TeamColor getPlayerColor() {
		return playerColor;
	}
}
