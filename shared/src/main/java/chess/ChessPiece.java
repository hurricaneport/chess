package chess;

import chess.pieceMoves.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessPiece {
	private final ChessGame.TeamColor pieceColor;

	private final ChessPiece.PieceType type;

	public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
		this.pieceColor = pieceColor;
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChessPiece piece = (ChessPiece) o;
		return pieceColor == piece.pieceColor && type == piece.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pieceColor, type);
	}

	@Override
	public String toString() {
		return "ChessPiece{" +
				"pieceColor=" + pieceColor +
				", type=" + type +
				'}';
	}

	/**
	 * @return Which team this chess piece belongs to
	 */
	public ChessGame.TeamColor getTeamColor() {
		return pieceColor;
	}

	/**
	 * @return which type of chess piece this piece is
	 */
	public PieceType getPieceType() {
		return type;
	}

	/**
	 * Calculates all the positions a chess piece can move to
	 * Does not take into account moves that are illegal due to leaving the king in
	 * danger
	 *
	 * @return Collection of valid moves
	 */
	public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
		ChessPiece piece = board.getPiece(myPosition);
		if (piece != this) {
			throw new IllegalArgumentException("Chess Piece at given position is not piece method was called on");
		}
		Collection<ChessMove> moves;
		ChessPieceMoves chessPieceMoves = switch (type) {
			case PAWN -> new PawnMoves(board, myPosition);
			case KING -> new KingMoves(board, myPosition);
			case QUEEN -> new QueenMoves(board, myPosition);
			case BISHOP -> new BishopMoves(board, myPosition);
			case KNIGHT -> new KnightMoves(board, myPosition);
			case ROOK -> new RookMoves(board, myPosition);
		};

		moves = chessPieceMoves.getMoves();
		return moves;
	}

	/**
	 * The various different chess piece options
	 */
	public enum PieceType {
		KING,
		QUEEN,
		BISHOP,
		KNIGHT,
		ROOK,
		PAWN
	}
}
