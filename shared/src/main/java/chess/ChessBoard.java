package chess;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] pieces = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (position == null) {
            return null;
        }
        return pieces[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Updates the board to make a given chess move.
     * @param move Chess Move to be made
     * @throws InvalidMoveException If no piece found at starting location. This function does not check if Chess Move is otherwise valid, and moves should be checked before passing
     */
    public void makeMove(ChessMove move) throws InvalidMoveException{
        if (getPiece(move.getStartPosition()) == null) {
            throw (new InvalidMoveException("Invalid move from " + move.getStartPosition().getAlgebraicNotation() + " to " + move.getEndPosition().getAlgebraicNotation() + ". No piece at start location"));
        }
        pieces[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1] = getPiece(move.getStartPosition());
        pieces[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;

        if (move.getPromotionPiece() != null) {
            pieces[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1] = new ChessPiece(getPiece(move.getEndPosition()).getTeamColor(), move.getPromotionPiece());
        }
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //White First Row
        pieces[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        pieces[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        pieces[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        pieces[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        pieces[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        pieces[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        pieces[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        pieces[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        //White Pawns
        for (int i = 0; i < 8; i++) {
            pieces[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        //Black First Row
        pieces[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        pieces[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        pieces[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        pieces[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        pieces[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        pieces[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        pieces[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        pieces[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        //Black Pawns
        for (int i = 0; i < 8; i++) {
            pieces[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    public HashMap<ChessPosition,ChessPiece> getPieces() {
        HashMap<ChessPosition, ChessPiece> piecesMap = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                if (pieces[i][k] != null) {
                    piecesMap.put(new ChessPosition(i + 1, k + 1), pieces[i][k]);
                }
            }
        }
        return piecesMap;
    }
}
