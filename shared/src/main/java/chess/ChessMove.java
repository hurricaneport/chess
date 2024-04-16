package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessMove {
	private final ChessPosition startPosition;
	private final ChessPosition endPosition;
	private final ChessPiece.PieceType promotionPiece;

	public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
					 ChessPiece.PieceType promotionPiece) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.promotionPiece = promotionPiece;
	}

	public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
		this(startPosition, endPosition, null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChessMove chessMove = (ChessMove) o;
		return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
	}

	@Override
	public int hashCode() {
		return Objects.hash(startPosition, endPosition, promotionPiece);
	}

	@Override
	public String toString() {
		return "ChessMove{" +
				"startPosition=" + startPosition +
				", endPosition=" + endPosition +
				", promotionPiece=" + promotionPiece +
				'}';
	}

	/**
	 * @return ChessPosition of starting location
	 */
	public ChessPosition getStartPosition() {
		return startPosition;
	}

	/**
	 * @return ChessPosition of ending location
	 */
	public ChessPosition getEndPosition() {
		return endPosition;
	}

	/**
	 * Gets the type of piece to promote a pawn to if pawn promotion is part of this
	 * chess move
	 *
	 * @return Type of piece to promote a pawn to, or null if no promotion
	 */
	public ChessPiece.PieceType getPromotionPiece() {
		return promotionPiece;
	}

	public static ChessMove fromStringNotation(String inputMove) throws IllegalArgumentException {
		if (inputMove == null) {
			throw new IllegalArgumentException("Input cannot be null");
		}
		if (inputMove.length() != 4 && inputMove.length() != 5) {
			throw new IllegalArgumentException("Input wrong length");
		}

		ChessPosition startPosition = ChessPosition.fromCoordinates(inputMove.substring(0, 2));
		ChessPosition endPosition = ChessPosition.fromCoordinates(inputMove.substring(2, 4));
		ChessPiece.PieceType promotionPiece = null;
		if (inputMove.length() == 5) {
			switch (inputMove.substring(4, 5)) {
				case "q" -> promotionPiece = ChessPiece.PieceType.QUEEN;
				case "b" -> promotionPiece = ChessPiece.PieceType.BISHOP;
				case "n" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
				case "r" -> promotionPiece = ChessPiece.PieceType.ROOK;
				default -> throw new IllegalArgumentException("Promotion piece must be a valid piece type");
			}
		}
		return new ChessMove(startPosition, endPosition, promotionPiece);
	}
}
