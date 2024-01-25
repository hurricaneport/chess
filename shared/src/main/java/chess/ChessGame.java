package chess;

import java.util.Collection;
import java.util.HashMap;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTurn;
    private ChessBoard chessBoard;

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public ChessGame() {
        currentTurn = TeamColor.WHITE;
        chessBoard = new ChessBoard();
        chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
    return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (chessBoard.getPiece(startPosition) == null ) {
            return null;
        }
        Collection<ChessMove> moves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);

        for (ChessMove move : moves) {
            ChessBoard testBoard = null; //copy current board
        }

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw (new InvalidMoveException("Move from" + move.getStartPosition().getAlgebraicNotation() + " to " + move.getEndPosition().getAlgebraicNotation() + " is not valid."));
        }
        else if (chessBoard.getPiece(move.getStartPosition()).getTeamColor() != currentTurn) {
            throw new InvalidMoveException(chessBoard.getPiece(move.getStartPosition()).getTeamColor() + " cannot move, it is not their turn");
        }
        else {
            chessBoard.makeMove(move);
        }

        nextTurn();
    }

    private void nextTurn() {
        if (currentTurn == TeamColor.BLACK) currentTurn = TeamColor.WHITE;
        else currentTurn = TeamColor.BLACK;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Returns if a given position on the provided board is in check. If the given position does not have a king of the provided color, throws InvalidMoveException
     * @param teamColor Color of the King to check against
     * @param chessPosition Position to check
     * @param chessBoard given board to check
     * @throws InvalidMoveException when the given position does not have a king of the provided color
     * @return True if position would be in check with the given board, false otherwise
     */
    private boolean positionIsInCheck(TeamColor teamColor, ChessPosition chessPosition, ChessBoard chessBoard) throws InvalidMoveException {
        if (chessBoard.getPiece(chessPosition).getPieceType() != ChessPiece.PieceType.KING || chessBoard.getPiece(chessPosition).getTeamColor() != teamColor) {
            throw new InvalidMoveException("Square does not contain a king of the given color");
        }
        HashMap<ChessPosition, ChessPiece> pieces = chessBoard.getPieces();

        for (ChessPosition position : pieces.keySet()) {
            ChessPiece currentPiece = pieces.get(position);
            if (currentPiece.getTeamColor() != teamColor) {
                Collection<ChessMove> moves = currentPiece.pieceMoves(chessBoard, position);
                for (ChessMove move : moves) {
                    if (move.getEndPosition().equals(chessPosition)) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
