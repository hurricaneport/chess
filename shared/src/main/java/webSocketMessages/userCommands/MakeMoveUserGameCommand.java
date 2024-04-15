package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveUserGameCommand extends UserGameCommand {
	private final ChessMove move;

	public MakeMoveUserGameCommand(String authToken, Integer gameID, ChessMove chessMove) {
		super(authToken, CommandType.MAKE_MOVE, gameID);
		this.move = chessMove;
	}

	public ChessMove getChessMove() {
		return move;
	}
}
