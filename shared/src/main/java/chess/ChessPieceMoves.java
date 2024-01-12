package chess;

import java.util.Collection;

public abstract class ChessPieceMoves {

    Collection<ChessMove> moves;
    final ChessBoard chessBoard;
    final ChessPosition position;
    public ChessPieceMoves(ChessBoard chessBoard, ChessPosition chessPosition) {
        this.chessBoard = chessBoard;
        this.position = chessPosition;
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
