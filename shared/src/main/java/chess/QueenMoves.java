package chess;

public class QueenMoves extends ChessPieceMoves{
    public QueenMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public void findMoves() {
        BishopMoves bishopMoves = new BishopMoves(chessBoard, position);
        moves.addAll(bishopMoves.getMoves());

        RookMoves rookMoves = new RookMoves(chessBoard, position);
        moves.addAll(rookMoves.getMoves());
    }
}
