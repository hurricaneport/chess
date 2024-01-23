package chess;

import java.util.ArrayList;

public class KingMoves extends ChessPieceMoves{

    private static final int[][] KING_MOVES = {
            {1, 1},
            {1, 0},
            {1, -1},
            {0, -1},
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, 1}};
    public KingMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public void findMoves() {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            ChessPosition currentPosition = ChessPosition.createValidPosition(position.getRow() + KING_MOVES[i][0], position.getColumn() + KING_MOVES[i][1]);
            if (currentPosition != null) {
                possiblePositions.add(currentPosition);
            }
        }

        for (ChessPosition possiblePosition : possiblePositions) {
            if (chessBoard.getPiece(possiblePosition) == null) {
                moves.add(new ChessMove(position, possiblePosition));
            } else if (chessBoard.getPiece(possiblePosition).getTeamColor() != chessBoard.getPiece(position).getTeamColor()) {
                moves.add(new ChessMove(position, possiblePosition));
            }
        }

    }
}
