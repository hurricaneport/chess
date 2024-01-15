package chess;

public class BishopMoves extends ChessPieceMoves {
    public BishopMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public void findMoves() {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        //Upper-Right Diagonal
        while (currentRow < 8 && currentCol < 8) {
            currentRow++;
            currentCol++;
            ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);
            if (chessBoard.getPiece(nextPosition) != null) {
                if (chessBoard.getPiece(nextPosition).pieceColor != chessBoard.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, nextPosition));
                }
                break;
            }
            else {
                moves.add(new ChessMove(position, nextPosition));
            }

        }

        //Lower-Right Diagonal
        currentRow = position.getRow();
        currentCol = position.getColumn();
        while (currentRow > 1 && currentCol < 8) {
            currentRow--;
            currentCol++;
            ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);
            if (chessBoard.getPiece(nextPosition) != null) {
                if (chessBoard.getPiece(nextPosition).pieceColor != chessBoard.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, nextPosition));
                }
                break;
            }
            else {
                moves.add(new ChessMove(position, nextPosition));
            }

        }

        //Lower-Left Diagonal
        currentRow = position.getRow();
        currentCol = position.getColumn();
        while (currentRow > 1 && currentCol > 1) {
            currentRow--;
            currentCol--;
            ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);
            if (chessBoard.getPiece(nextPosition) != null) {
                if (chessBoard.getPiece(nextPosition).pieceColor != chessBoard.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, nextPosition));
                }
                break;
            }
            else {
                moves.add(new ChessMove(position, nextPosition));
            }

        }

        //Upper-Left Diagonal
        currentRow = position.getRow();
        currentCol = position.getColumn();
        while (currentRow < 8 && currentCol > 1) {
            currentRow++;
            currentCol--;
            ChessPosition nextPosition = new ChessPosition(currentRow, currentCol);
            if (chessBoard.getPiece(nextPosition) != null) {
                if (chessBoard.getPiece(nextPosition).pieceColor != chessBoard.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, nextPosition));
                }
                break;
            }
            else {
                moves.add(new ChessMove(position, nextPosition));
            }

        }
    }
}
