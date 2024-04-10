package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveUserGameCommand extends UserGameCommand {
	private final ChessMove chessMove;

	public MakeMoveUserGameCommand(String authToken, Integer gameID, ChessMove chessMove) {
		super(authToken, CommandType.MAKE_MOVE, gameID);
		this.chessMove = chessMove;
	}

	public ChessMove getChessMove() {
		return chessMove;
	}
}
