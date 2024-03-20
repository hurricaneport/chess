package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import static chess.ChessGame.TeamColor.BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class GraphicsUtils {
	public static String[][] getPieceCharacters(ChessBoard chessBoard) {
		ChessPiece[][] chessPieces = chessBoard.getPieces();
		String[][] pieceCharacters = new String[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				pieceCharacters[i][j] = getPieceChar(chessPieces[i][j]);
			}
		}

		return pieceCharacters;
	}

	public static String[][] getPieceCharactersReversed(ChessBoard chessBoard) {
		String[][] pieceCharacters = getPieceCharacters(chessBoard);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				String temp = pieceCharacters[i][j];
				pieceCharacters[i][j] = pieceCharacters[i][7 - j];
				pieceCharacters[i][7 - j] = temp;
			}
		}

		for (int i = 0; i < 4; i++) {
			String[] temp = pieceCharacters[i];
			pieceCharacters[i] = pieceCharacters[7 - i];
			pieceCharacters[7 - i] = temp;

		}

		return pieceCharacters;
	}

	private static String getPieceChar(ChessPiece chessPiece) {
		if (chessPiece == null) {
			return " ";
		}
		ChessGame.TeamColor teamColor = chessPiece.getTeamColor();
		String pieceChar;
		switch (chessPiece.getPieceType()) {
			case KING -> pieceChar = "K";
			case QUEEN -> pieceChar = "Q";
			case BISHOP -> pieceChar = "B";
			case KNIGHT -> pieceChar = "N";
			case ROOK -> pieceChar = "R";
			case PAWN -> pieceChar = "P";
			case null -> throw new RuntimeException("Piece has color unassigned");
		}
		return getColoredPieceChar(pieceChar, teamColor);
	}

	private static String getColoredPieceChar(String pieceChar, ChessGame.TeamColor teamColor) {
		String colorCode;
		if (teamColor == BLACK) {
			colorCode = SET_TEXT_COLOR_RED;
		} else {
			colorCode = SET_TEXT_COLOR_BLUE;
		}

		return colorCode + pieceChar;
	}
}
