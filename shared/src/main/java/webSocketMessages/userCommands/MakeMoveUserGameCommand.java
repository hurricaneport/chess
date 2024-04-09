package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveUserGameCommand extends UserGameCommand {
	private final Integer gameID;
	private final ChessMove chessMove;

	public MakeMoveUserGameCommand(String authToken, Integer gameID, ChessMove chessMove) {
		super(authToken, CommandType.MAKE_MOVE);
		this.gameID = gameID;
		this.chessMove = chessMove;
	}

	public Integer getGameID() {
		return gameID;
	}

	public ChessMove getChessMove() {
		return chessMove;
	}
}