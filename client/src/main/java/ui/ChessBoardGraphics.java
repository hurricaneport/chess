package ui;

import chess.ChessBoard;

public class ChessBoardGraphics {

    private static final String[] columnLabels = {"a","b","c","d","e","f","g","h"};
    private static final String [] rowLabels = {"1","2","3","4","5","6","7","8"};
    private static final String separator = " ";
    public static void drawChessBoard(ChessBoard chessBoard, boolean isForward) {
        drawLabels(isForward);

        String[][] pieceChars = isForward ? GraphicsUtils.getPieceCharacters(chessBoard) : GraphicsUtils.getPieceCharactersReversed(chessBoard);

        String[] rowLabels1;
        if (!isForward) {
            rowLabels1 = new String[8];

            for (int i = 0; i < 4; i++) {
                rowLabels1[i] = rowLabels[7 - i];
                rowLabels1[7 - i] = rowLabels[i];
            }
        }
        else {
            rowLabels1 = rowLabels;
        }

        boolean isEvenRow = isForward;
        for (int i = 7; i >= 0; i--) {
            drawRows(rowLabels1[i], pieceChars[i], isEvenRow);
            isEvenRow = !isEvenRow;
        }

        drawLabels(isForward);
        System.out.print(EscapeSequences.RESET_ALL + "\n");

    }

    private static void drawLabels(boolean isForward) {
        String[] labels1;
        if (!isForward) {
            labels1 = new String[8];
            for (int i = 0; i < 4; i++) {
                labels1[i] = columnLabels[7 - i];
                labels1[7 - i] = columnLabels[i];
            }
        }
        else {
            labels1 = columnLabels;
        }

        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(separator + separator + separator);
        for (int i = 0; i < 8; i++) {
            System.out.print(separator + labels1[i] + separator);
        }
        System.out.print(separator + separator + separator + EscapeSequences.RESET_ALL + "\n");
    }

    private static void drawRows(String rowLabel, String[] pieceChars, boolean isEvenRow) {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(separator + rowLabel + separator);

         boolean whiteSquare = isEvenRow;

         for (int i = 0; i < 8; i++) {
             String backgroundColor = whiteSquare ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
             System.out.print(backgroundColor + separator + pieceChars[i] + separator);
             whiteSquare = ! whiteSquare;
         }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(separator + rowLabel + separator + EscapeSequences.RESET_ALL + "\n");
    }
}
