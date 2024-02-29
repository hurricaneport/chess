package chess.pieceMoves;

import chess.*;

public class PawnMoves extends ChessPieceMoves{
    public PawnMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public void findMoves() {
        int colorInt = 1;
        int startRow = 2;
        int endRow = 8;
        if (chessBoard.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
            colorInt = -1;
            startRow = 7;
            endRow = 1;
        }
        //forward move
        ChessPosition forward = ChessPosition.createValidPosition(position.getRow() + colorInt, position.getColumn());
        if (forward != null && chessBoard.getPiece(forward) == null) {
            if (forward.getRow() == endRow) {
                addPromotionMoves(forward);
            } else {
                moves.add(new ChessMove(position, forward));
            }
            //double forward
            ChessPosition doubleForward = ChessPosition.createValidPosition(position.getRow() + 2 * colorInt, position.getColumn());
            if (doubleForward != null && position.getRow() == startRow && chessBoard.getPiece(doubleForward) == null) {
                moves.add(new ChessMove(position, doubleForward));
            }

        }

        //left diagonal
        ChessPosition leftDiagonal = ChessPosition.createValidPosition(position.getRow() + colorInt, position.getColumn() - 1);
        if (leftDiagonal != null && chessBoard.getPiece(leftDiagonal) != null && chessBoard.getPiece(leftDiagonal).getTeamColor() != chessBoard.getPiece(position).getTeamColor()) {
            if (leftDiagonal.getRow() == endRow) {
                addPromotionMoves(leftDiagonal);
            } else {
                moves.add(new ChessMove(position, leftDiagonal));
            }
        }

        //right diagonal
        ChessPosition rightDiagonal = ChessPosition.createValidPosition(position.getRow() + colorInt, position.getColumn() + 1);
        if (rightDiagonal != null && chessBoard.getPiece(rightDiagonal) != null && chessBoard.getPiece(rightDiagonal).getTeamColor() != chessBoard.getPiece(position).getTeamColor()) {
            if (rightDiagonal.getRow() == endRow) {
                addPromotionMoves(rightDiagonal);
            } else {
                moves.add(new ChessMove(position, rightDiagonal));
            }
        }

    }

    private void addPromotionMoves (ChessPosition endPosition) {
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
    }
}
