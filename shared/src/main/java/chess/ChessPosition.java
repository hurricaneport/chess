package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessPosition {

	private final int row;
	private final int col;

	public ChessPosition(int row, int col) throws IllegalArgumentException {
		if (row > 8 || row < 1 || col > 8 || col < 1) {
			throw new IllegalArgumentException("Not a valid chess position");
		}

		this.row = row;
		this.col = col;
	}

	/**
	 * Returns a chess position if position is valid, otherwise returns null
	 *
	 * @param row row to add to created ChessPosition
	 * @param col column to add to created ChessPosition
	 * @return ChessPosition if positions valid, otherwise null
	 */
	public static ChessPosition createValidPosition(int row, int col) {
		if (!(row > 8 || row < 1 || col > 8 || col < 1)) {
			return new ChessPosition(row, col);
		} else {
			return null;
		}
	}

	public static ChessPosition fromCoordinates(String position) throws IllegalArgumentException {
		if (position == null) {
			throw new IllegalArgumentException("Input cannot be null");
		}
		if (position.length() != 2) {
			throw new IllegalArgumentException("String is incorrect length");
		}
		int col = (int) position.charAt(0) - 96;
		int row = Integer.parseInt(String.valueOf(position.charAt(1)));
		if (col < 1 || col > 8 || row < 1 || row > 8) {
			throw new IllegalArgumentException("Invalid coordinates");
		}
		return new ChessPosition(row, col);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChessPosition that = (ChessPosition) o;
		return row == that.row && col == that.col;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, col);
	}

	@Override
	public String toString() {
		return "ChessPosition{" +
				"row=" + row +
				", col=" + col +
				'}';
	}

	/**
	 * @return which row this position is in
	 * 1 codes for the bottom row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return which column this position is in
	 * 1 codes for the left row
	 */
	public int getColumn() {
		return col;
	}

	public String getAlgebraicNotation() {
		char colChar = (char) (col + 96);
		return colChar + "" + row;
	}
}
