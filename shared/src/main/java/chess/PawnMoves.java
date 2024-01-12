package chess;

public class PawnMoves extends ChessPieceMoves{
    public PawnMoves(ChessBoard chessBoard, ChessPosition position) {
        super(chessBoard, position);
    }
    @Override
    public void findMoves() {
        ChessPiece piece = chessBoard.getPiece(position);
        int colorInt = 1;
        int startingRow = 2;
        int endingRow = 8;
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            colorInt = -1;
            startingRow = 7;
            endingRow = 1;
        }
        //positions to check later
        //No need to check forward positions, as a double forward can only happen on the first row, and a single forward would only result in an error on the last row, where a pawn cannot exist
        ChessPosition front = new ChessPosition(position.getRow() + colorInt, position.getColumn());
        ChessPosition doubleFront = new ChessPosition(position.getRow() + 2 * colorInt, position.getColumn());

        if (position.getColumn() > 1) {
            ChessPosition leftDiagonal = new ChessPosition(position.getRow() + colorInt, position.getColumn() - 1);
        }
        else {
            ChessPosition leftDiagonal = null;
        }

        if (position.getColumn() < 8) {
            ChessPosition rightDiagonal = new ChessPosition(position.getRow() + colorInt, position.getColumn() + 1);
        }
        else {
            ChessPosition rightDiagonal = null;
        }

        //check if pawn is in starting position and offer first move
        if (position.getRow() == startingRow) {
            //check that both destination and passing square are empty
            if (chessBoard.getPiece(front) == null && chessBoard.getPiece(doubleFront) == null) {
                moves.add(new ChessMove(position, doubleFront));
            }
        }

        //add normal forward move
        if (chessBoard.getPiece(front) == null) {
            //if move results in promotion, generate all
            if (front.getColumn() == endingRow) {
                addPromotionMoves(position, front);
            }
            else {
                moves.add(new ChessMove(position, front));
            }
        }

        //add diagonals


    }

    private void addPromotionMoves(ChessPosition startPosition, ChessPosition endPosition) {
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
    }
}
