package client;

import api.HTTPConnectionException;
import api.facade.ServerFacade;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import ui.ChessBoardGraphics;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.LoadGameServerMessage;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static chess.ChessGame.TeamColor.BLACK;

public class InGameMenu implements ServerMessageObserver {
	Scanner scanner = new Scanner(System.in);
	private ChessGame currentGame = new ChessGame();
	private ChessGame.TeamColor teamColor;
	private volatile boolean awaitBoard = false;
	private volatile boolean awaitError = false;

	CountDownLatch threadReady;
	private boolean isSpectator = false;
	Integer currentGameID;
	Menu menu;
	ServerFacade serverFacade;

	public InGameMenu(Menu menu) {
		this.menu = menu;

	}

	public void inGameMenu(int gameID, ServerFacade serverFacade, boolean isSpectator, ChessGame.TeamColor teamColor) {
		awaitBoard = true;
		awaitError = true;

		while (awaitBoard && awaitError) Thread.onSpinWait();

		if (!awaitError) {
			awaitBoard = false;
			menu.postLogin();
			return;
		} else {
			awaitError = false;
		}

		currentGameID = gameID;
		this.serverFacade = serverFacade;
		this.isSpectator = isSpectator;
		this.teamColor = teamColor;
		inGameMenu();
	}

	private void inGameMenu() {

		System.out.print("""
				Please Select an Option
				1. Help
				2. Make Move
				3. Highlight Legal Moves
				4. Redraw Board
				5. Resign
				6. Leave
				                
				""");

		String result = scanner.nextLine();

		switch (result) {
			case "1" -> inGameHelp();
			case "2" -> makeMove();
			case "3" -> highlightLegalMoves();
			case "4" -> redrawBoard();
			case "5" -> resign();
			case "6" -> leaveGame();
			default -> {
				System.out.print("'" + result + "' is not valid input, please select a valid option or press 1 for help\n\n");
				inGameMenu();
			}
		}
	}

	private void inGameHelp() {
		System.out.print("""
				Help:
				Please enter into the terminal the number of the option you wish to select then press 'Enter'
				Press '1' to repeat this message
				Press '2' to make a chess move
				Press '3' to highlight all possible moves
				Press '4' to redraw the chess board
				Press '5' to resign from the game
				Press '6' to leave the game and return to the menu. You can always join again later.
				                
				""");
		inGameMenu();
	}

	private void makeMove() {
		if (isSpectator) {
			System.out.print("Spectators cannot make moves. Please join a game as a player to play the game.\n\n");
			inGameMenu();
		}

		System.out.print("""
				Make a move:
				Please enter the move you would like to make in the following format with no spaces:
				'starting coordinate' + 'ending coordinate' + 'promotion piece letter (optional)'
				Invalid moves will result in an error and you will be asked to try again.
								
				""");

		String input = scanner.nextLine();

		try {
			serverFacade.makeMove(ChessMove.fromStringNotation(input), currentGameID);

			inGameMenu();

		} catch (HTTPConnectionException e) {
			System.out.print("Connection error, please try again\n\n");
			inGameMenu();
		} catch (IllegalArgumentException e) {
			System.out.print(e.getMessage() + " Please enter a valid move.\n\n");
			inGameMenu();
		}
	}

	private void highlightLegalMoves() {
		System.out.print("Please enter the coordinates of the starting space you would like to see valid moves for.\n\n");
		String position = scanner.nextLine();
		try {
			ChessPosition startingSpace = ChessPosition.fromCoordinates(position);
			ChessBoardGraphics.drawChessBoard(currentGame.getBoard(), teamColor != BLACK, startingSpace, currentGame.validMoves(startingSpace));
			inGameMenu();
		} catch (IllegalArgumentException e) {
			System.out.print(e.getMessage() + " Please enter a valid position.\n\n");
		}

	}

	private void redrawBoard() {
		ChessBoardGraphics.drawChessBoard(currentGame.getBoard(), teamColor != BLACK, null, null);

		inGameMenu();
	}

	private void resign() {
		if (isSpectator) {
			System.out.print("Spectators cannot resign. Please join a game as a player to play the game.\n\n");
			inGameMenu();
		}

		try {
			serverFacade.resign(currentGameID);
			inGameMenu();
		} catch (HTTPConnectionException e) {
			System.out.print("Could not resign, please try again");
			inGameMenu();
		}
	}

	private void leaveGame() {
		try {
			serverFacade.leaveGame(currentGameID);
			teamColor = null;
			currentGame = null;
			currentGameID = null;
			isSpectator = false;

			System.out.print("Left game, returning to main menu.\n\n");
			menu.postLogin();
		} catch (HTTPConnectionException e) {
			System.out.print("Could not leave game, please try again.\n\n");
			inGameMenu();
		}
	}

	@Override
	public void notify(ServerMessage serverMessage) {
		switch (serverMessage.getServerMessageType()) {
			case LOAD_GAME -> loadGame((LoadGameServerMessage) serverMessage);
			case ERROR -> showError((ErrorServerMessage) serverMessage);
			case NOTIFICATION -> showNotification((NotificationServerMessage) serverMessage);
		}
	}

	private void loadGame(LoadGameServerMessage loadGameServerMessage) {
		currentGame = loadGameServerMessage.getGame();
		boolean isForward = (teamColor != BLACK);
		ChessBoardGraphics.drawChessBoard(currentGame.getBoard(), isForward, null, null);
		awaitBoard = false;
		threadReady.countDown();
	}

	public void showNotification(NotificationServerMessage notificationServerMessage) {
		System.out.print(notificationServerMessage.getMessage() + "\n");
	}

	private void showError(ErrorServerMessage errorServerMessage) {
		System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + errorServerMessage.getErrorMessage() + EscapeSequences.RESET_ALL + "\n");
		awaitBoard = false;
		threadReady.countDown();
	}
}
