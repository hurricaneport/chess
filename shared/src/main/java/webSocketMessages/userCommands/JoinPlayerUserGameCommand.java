package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerUserGameCommand extends UserGameCommand {
	private final ChessGame.TeamColor playerColor;

	public JoinPlayerUserGameCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
		super(authToken, CommandType.JOIN_PLAYER, gameID);
		this.playerColor = playerColor;
	}

	public ChessGame.TeamColor getPlayerColor() {
		return playerColor;
	}
}
