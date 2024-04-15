package ui;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChessBoardGraphics {

	private static final String[] columnLabels = {"a", "b", "c", "d", "e", "f", "g", "h"};
	private static final String[] rowLabels = {"1", "2", "3", "4", "5", "6", "7", "8"};
	private static final String separator = " ";

	public static void drawChessBoard(ChessBoard chessBoard, boolean isForward, ChessPosition startingSpace, Collection<ChessMove> legalMoves) {
		if (legalMoves == null) {
			legalMoves = new HashSet<>();
		}
		drawLabels(isForward);

		String[][] pieceChars = isForward ? GraphicsUtils.getPieceCharacters(chessBoard) : GraphicsUtils.getPieceCharactersReversed(chessBoard);

		String[] rowLabels1;
		if (!isForward) {
			rowLabels1 = new String[8];

			for (int i = 0; i < 4; i++) {
				rowLabels1[i] = rowLabels[7 - i];
				rowLabels1[7 - i] = rowLabels[i];
			}
		} else {
			rowLabels1 = rowLabels;
		}

		Set<ChessPosition> endingSpaces = new HashSet<>();

		for (ChessMove move : legalMoves) {
			endingSpaces.add(move.getEndPosition());
		}

		boolean isEvenRow = isForward;
		for (int i = 7; i >= 0; i--) {
			drawRows(rowLabels1[i], pieceChars[i], isEvenRow, i, isForward, startingSpace, endingSpaces);
			isEvenRow = !isEvenRow;
		}

		drawLabels(isForward);
		System.out.print(EscapeSequences.RESET_ALL + "\n");

	}

	private static void drawLabels(boolean isForward) {
		String[] labels1;
		if (!isForward) {
			labels1 = new String[8];
			for (int i = 0; i < 4; i++) {
				labels1[i] = columnLabels[7 - i];
				labels1[7 - i] = columnLabels[i];
			}
		} else {
			labels1 = columnLabels;
		}

		System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
		System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
		System.out.print(separator + separator + separator);
		for (int i = 0; i < 8; i++) {
			System.out.print(separator + labels1[i] + separator);
		}
		System.out.print(separator + separator + separator + EscapeSequences.RESET_ALL + "\n");
	}

	private static void drawRows(String rowLabel, String[] pieceChars, boolean isEvenRow, int currentRow, boolean isForward, ChessPosition startingSpace, Set<ChessPosition> endSpaces) {
		System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
		System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
		System.out.print(separator + rowLabel + separator);

		boolean whiteSquare = isEvenRow;

		for (int i = 0; i < 8; i++) {
			String whiteSquareColor = EscapeSequences.SET_BG_COLOR_WHITE;
			String blackSquareColor = EscapeSequences.SET_BG_COLOR_BLACK;
			if (endSpaces.contains(indexToPosition(currentRow, i, isForward))) {
				blackSquareColor = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
				whiteSquareColor = EscapeSequences.SET_BG_COLOR_GREEN;
			} else if (Objects.equals(indexToPosition(currentRow, i, isForward), startingSpace)) {
				whiteSquareColor = EscapeSequences.SET_BG_COLOR_YELLOW;
				blackSquareColor = EscapeSequences.SET_BG_COLOR_YELLOW;
			}
			String backgroundColor = whiteSquare ? whiteSquareColor : blackSquareColor;
			System.out.print(backgroundColor + separator + pieceChars[i] + separator);
			whiteSquare = !whiteSquare;
		}
		System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
		System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
		System.out.print(separator + rowLabel + separator + EscapeSequences.RESET_ALL + "\n");
	}

	private static ChessPosition indexToPosition(int rowIndex, int colIndex, boolean isForward) {
		//TODO: Implement this
		return null;
	}
}
