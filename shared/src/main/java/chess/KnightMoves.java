package chess;

import java.util.ArrayList;

public class KnightMoves extends ChessPieceMoves {
    public KnightMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public void findMoves() {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            try {
                ChessPosition currentPosition = new ChessPosition(position.getRow() + KNIGHT_MOVES[i][0], position.getColumn() + KNIGHT_MOVES[i][1]);
                possiblePositions.add(currentPosition);
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException " + e.getMessage());
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

    public static final int[][] KNIGHT_MOVES = {
            {2, -1},
            {2, 1},
            {1, 2},
            {-1, 2},
            {-2, 1},
            {-2, -1},
            {1, -2},
            {-1, -2}};
}