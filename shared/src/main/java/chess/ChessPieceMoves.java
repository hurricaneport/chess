package chess;

import java.util.Collection;
import java.util.HashSet;

public abstract class ChessPieceMoves {

    Collection<ChessMove> moves = new HashSet<>();
    final ChessBoard chessBoard;
    final ChessPosition position;
    public ChessPieceMoves(ChessBoard chessBoard, ChessPosition position) {
        this.chessBoard = chessBoard;
        this.position = position;
        findMoves();
    }

    /**
     *
     * @return Collection of moves for given piece
     */
    public Collection<ChessMove> getMoves() {
        return moves;
    }

    /**
     * Finds valid moves for given piece and position
     */
    public abstract void findMoves();
}
