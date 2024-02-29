package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents all moves a given piece can make at a certain position on the board
 */
public abstract class ChessPieceMoves {

    Collection<ChessMove> moves = new HashSet<>();
    final ChessBoard chessBoard;
    final ChessPosition position;
    public ChessPieceMoves(ChessBoard chessBoard, ChessPosition position) {
        this.chessBoard = chessBoard;
        this.position = position;
    }

    /**
     * Gets all moves that piece can make given its current position and the layout of the board
     *
     * @return Collection of moves for given piece
     */
    public Collection<ChessMove> getMoves() {
        if(moves.isEmpty()) {
            findMoves();
        }
        return moves;
    }

    /**
     * Finds valid moves for given piece and position
     */
    public abstract void findMoves();
}
