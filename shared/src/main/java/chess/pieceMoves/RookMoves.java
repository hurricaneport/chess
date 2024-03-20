package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

public class RookMoves extends ChessPieceMoves {
	public RookMoves(ChessBoard board, ChessPosition position) {
		super(board, position);
	}

	@Override
	public void findMoves() {

		//Forward Line
		int currentRow = position.getRow();
		int currentCol = position.getColumn();
		while (currentRow < 8) {
			currentRow++;
			ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);

			if (chessBoard.getPiece(nextPosition) != null) {
				if (chessBoard.getPiece(nextPosition).getTeamColor() != chessBoard.getPiece(position).getTeamColor()) {
					moves.add(new ChessMove(position, nextPosition));
				}
				break;
			} else {
				moves.add(new ChessMove(position, nextPosition));
			}
		}

		//Right Line
		currentRow = position.getRow();
		while (currentCol < 8) {
			currentCol++;
			ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);

			if (chessBoard.getPiece(nextPosition) != null) {
				if (chessBoard.getPiece(nextPosition).getTeamColor() != chessBoard.getPiece(position).getTeamColor()) {
					moves.add(new ChessMove(position, nextPosition));
				}
				break;
			} else {
				moves.add(new ChessMove(position, nextPosition));
			}
		}

		//Bottom Line
		currentCol = position.getColumn();
		while (currentRow > 1) {
			currentRow--;
			ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);

			if (chessBoard.getPiece(nextPosition) != null) {
				if (chessBoard.getPiece(nextPosition).getTeamColor() != chessBoard.getPiece(position).getTeamColor()) {
					moves.add(new ChessMove(position, nextPosition));
				}
				break;
			} else {
				moves.add(new ChessMove(position, nextPosition));
			}
		}

		//Left Line
		currentRow = position.getRow();
		while (currentCol > 1) {
			currentCol--;
			ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);

			if (chessBoard.getPiece(nextPosition) != null) {
				if (chessBoard.getPiece(nextPosition).getTeamColor() != chessBoard.getPiece(position).getTeamColor()) {
					moves.add(new ChessMove(position, nextPosition));
				}
				break;
			} else {
				moves.add(new ChessMove(position, nextPosition));
			}
		}
	}
}
