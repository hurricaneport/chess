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
        ChessPosition front;
        ChessPosition doubleFront;
        ChessPosition leftDiagonal = null;
        ChessPosition rightDiagonal = null;
        //No need to check forward positions, as a double forward can only happen on the first row, and a single forward would only result in an error on the last row, where a pawn cannot exist
        front = new ChessPosition(position.getRow() + colorInt, position.getColumn());


        if (position.getColumn() > 1) {
            leftDiagonal = new ChessPosition(position.getRow() + colorInt, position.getColumn() - 1);
        }

        if (position.getColumn() < 8) {
            rightDiagonal = new ChessPosition(position.getRow() + colorInt, position.getColumn() + 1);
        }

        //check if pawn is in starting position and offer first move
        if (position.getRow() == startingRow) {

            doubleFront = new ChessPosition(position.getRow() + 2 * colorInt, position.getColumn());
            //check that both destination and passing square are empty
            if (chessBoard.getPiece(front) == null && chessBoard.getPiece(doubleFront) == null) {
                moves.add(new ChessMove(position, doubleFront));
            }
        }

        //add normal forward move
        if (chessBoard.getPiece(front) == null) {
            //if move results in promotion, generate all
            if (front.getRow() == endingRow) {
                addPromotionMoves(front);
            }
            else {
                moves.add(new ChessMove(position, front));
            }
        }

        //add diagonals
        if (chessBoard.getPiece(leftDiagonal) != null && leftDiagonal != null) {
            if (chessBoard.getPiece(leftDiagonal).getTeamColor() != piece.getTeamColor()) {
                if (leftDiagonal.getRow() == endingRow) {
                    addPromotionMoves(leftDiagonal);
                }
                else {
                    moves.add(new ChessMove(position, leftDiagonal));
                }
            }
        }

        if (chessBoard.getPiece(rightDiagonal) != null && rightDiagonal != null) {
            if (chessBoard.getPiece(rightDiagonal).getTeamColor() != piece.getTeamColor()) {
                if (rightDiagonal.getRow() == endingRow) {
                    addPromotionMoves(rightDiagonal);
                }
                else {
                    moves.add(new ChessMove(position, rightDiagonal));
                }
            }
        }

    }

    private void addPromotionMoves(ChessPosition endPosition) {
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
    }
}
