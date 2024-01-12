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
        ChessPosition front = new ChessPosition(position.getRow() + colorInt, position.getColumn());
        ChessPosition doubleFront = new ChessPosition(position.getRow() + 2 * colorInt, position.getColumn());
        ChessPosition leftDiagonal = new ChessPosition(position.getRow() + colorInt, position.getColumn() - 1);
        ChessPosition rightDiagonal = new ChessPosition(position.getRow() + colorInt, position.getColumn() + 1);

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
        if

    }

    private void addPromotionMoves(ChessPosition startPosition, ChessPosition endPosition) {
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
    }
}
