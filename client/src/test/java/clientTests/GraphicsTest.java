package clientTests;

import chess.ChessGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ui.ChessBoardGraphics.drawChessBoard;

public class GraphicsTest {

    @Test
    @DisplayName("Draw Chessboard Forward")
    public void drawChessboardForward() {
        drawChessBoard((new ChessGame()).getBoard(), true);
    }

    @Test
    @DisplayName("Draw Chessboard Reversed")
    public void drawChessboardReversed() {
        drawChessBoard((new ChessGame()).getBoard(), false);
    }
}
