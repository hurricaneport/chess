package chess;

public class BishopMoves extends ChessPieceMoves {
    public BishopMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public void findMoves() {
        int curentRow = position.getRow();
        int currentCol = position.getColumn();

        //Upper-Right Diagonal
        while (curentRow < 8 && currentCol < 8) {
            curentRow++;
            currentCol++;
            ChessPosition nextPosition = new ChessPosition(curentRow, currentCol);
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

        //Lower-Right DIagonal
        curentRow = position.getRow();
        currentCol = position.getColumn();
        while (curentRow > 1 && currentCol < 8) {
            curentRow--;
            currentCol++;
            ChessPosition nextPosition = new ChessPosition(curentRow, currentCol);
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
        curentRow = position.getRow();
        currentCol = position.getColumn();
        while (curentRow > 1 && currentCol > 1) {
            curentRow--;
            currentCol--;
            ChessPosition nextPosition = new ChessPosition(curentRow, currentCol);
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
        curentRow = position.getRow();
        currentCol = position.getColumn();
        while (curentRow < 8 && currentCol > 1) {
            curentRow++;
            currentCol--;
            ChessPosition nextPosition = new ChessPosition(curentRow, currentCol);
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
