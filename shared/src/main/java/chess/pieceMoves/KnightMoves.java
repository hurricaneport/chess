package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoves extends ChessPieceMoves {

    private static final int[][] KNIGHT_MOVES = {
            {2, -1},
            {2, 1},
            {1, 2},
            {-1, 2},
            {-2, 1},
            {-2, -1},
            {1, -2},
            {-1, -2}};
    public KnightMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public void findMoves() {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            ChessPosition currentPosition = ChessPosition.createValidPosition(position.getRow() + KNIGHT_MOVES[i][0], position.getColumn() + KNIGHT_MOVES[i][1]);
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