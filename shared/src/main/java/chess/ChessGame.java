package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTurn;
    private ChessBoard chessBoard;

    private final Deque<ChessMove> movesStack = new LinkedList<>();
    private final Deque<ChessPiece> piecesStack = new LinkedList<>();

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return currentTurn == chessGame.currentTurn && Objects.equals(chessBoard, chessGame.chessBoard) && Objects.equals(movesStack, chessGame.movesStack) && Objects.equals(piecesStack, chessGame.piecesStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTurn, chessBoard, movesStack, piecesStack);
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
        TeamColor pieceColor = chessBoard.getPiece(startPosition).getTeamColor();
        Iterator<ChessMove> movesIterator = moves.iterator();
        while (movesIterator.hasNext()) {
            ChessMove currentMove = movesIterator.next();
            makeTestMove(currentMove);
            if (isInCheck(pieceColor)) {
                movesIterator.remove();
            }
            undoMove();

        }

        return moves;
    }



    /**
     * Make valid move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException when move is not valid.
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw (new InvalidMoveException("Move from" + move.getStartPosition().getAlgebraicNotation() + " to " + move.getEndPosition().getAlgebraicNotation() + " is not valid."));
        }
        else if (chessBoard.getPiece(move.getStartPosition()).getTeamColor() != currentTurn) {
            throw new InvalidMoveException(chessBoard.getPiece(move.getStartPosition()).getTeamColor() + " cannot move, it is not their turn");
        }
        else {
            makeTestMove(move);
            nextTurn();
        }
    }

    /**
     * makes a move to be tested. Must be undone after with exeption of being called from makeMove()
     * @param move move to be made
     */
    public void makeTestMove(ChessMove move) {
        movesStack.addFirst(move);
        piecesStack.addFirst(chessBoard.getPiece(move.getEndPosition()));
        chessBoard.addPiece(move.getEndPosition(),chessBoard.getPiece(move.getStartPosition()));
        chessBoard.addPiece(move.getStartPosition(), null);

        TeamColor pieceColor = chessBoard.getPiece(move.getEndPosition()).getTeamColor();
        if (move.getPromotionPiece() != null) {
            chessBoard.addPiece(move.getEndPosition(), new ChessPiece(pieceColor,move.getPromotionPiece()));
        }
    }

    /**
     * Undoes the last made move. Used in conjunction with makeMove() to check moves.
     */
    public void undoMove() {
        ChessMove undoneMove = movesStack.removeFirst();
        ChessPiece capturedPiece = piecesStack.removeFirst();

        chessBoard.addPiece(undoneMove.getStartPosition(), chessBoard.getPiece(undoneMove.getEndPosition()));
        chessBoard.addPiece(undoneMove.getEndPosition(), capturedPiece);
        TeamColor pieceColor = chessBoard.getPiece(undoneMove.getStartPosition()).getTeamColor();
        if (undoneMove.getPromotionPiece() != null) {
            chessBoard.addPiece(undoneMove.getStartPosition(),new ChessPiece(pieceColor, ChessPiece.PieceType.PAWN));
        }
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
        Map<ChessPosition,ChessPiece> testBoardPieces = chessBoard.getPieces(teamColor);
        ChessPosition kingPosition = null;
        for (ChessPosition p : testBoardPieces.keySet()) {
            if (testBoardPieces.get(p).getPieceType() == ChessPiece.PieceType.KING) {
                kingPosition = p;
                break;
            }
        }
        TeamColor opponentColor = TeamColor.BLACK;
        if (teamColor == TeamColor.BLACK) {
            opponentColor = TeamColor.WHITE;
        }
        HashMap<ChessPosition, ChessPiece> pieces = chessBoard.getPieces(opponentColor);

        for (ChessPosition p : pieces.keySet()) {

            ChessPiece currentPiece = pieces.get(p);
            Collection<ChessMove> moves = currentPiece.pieceMoves(chessBoard, p);
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInStalemate(teamColor)) {
            return false;
        }
        Map<ChessPosition, ChessPiece> boardPieces = chessBoard.getPieces(teamColor);
        for (ChessPosition p : boardPieces.keySet()) {
            if (!validMoves(p).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Map<ChessPosition, ChessPiece> boardPieces = chessBoard.getPieces(teamColor);
        for (ChessPosition p : boardPieces.keySet()) {
            if (!validMoves(p).isEmpty()) {
                return false;
            }
        }
        return true;
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
