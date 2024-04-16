package client;

import api.HTTPConnectionException;
import api.HTTPResponseException;
import api.facade.ServerFacade;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import ui.ChessBoardGraphics;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.LoadGameServerMessage;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class Menu implements ServerMessageObserver {

	public Menu() {
		try {
			serverFacade = new ServerFacade(this);
		} catch (HTTPConnectionException e) {
			serverFacade = null;
		}
	}

	private final Scanner scanner = new Scanner(System.in);
	private ServerFacade serverFacade;
	private final ArrayList<GameData> games = new ArrayList<>();
	private ChessGame currentGame = new ChessGame();
	private ChessGame.TeamColor teamColor = WHITE;
	private volatile boolean awaitBoard = false;
	private volatile boolean awaitError = false;
	private boolean isSpectator = false;

	Integer currentGameID;

	public void run() {
		if (serverFacade == null) {
			System.out.print("Server could not be reached. Please try again later.\n\n");
			return;
		}
		System.out.print("Chess 2024\n\n");
		preLogin();
	}

	private void preLogin() {
		System.out.print("""
				Please Select an Option
				1. Help
				2. Quit
				3. Login
				4. Register

				""");

		String result = scanner.nextLine();
		switch (result) {
			case "1" -> preLoginHelp();
			case "2" -> {

			}
			case "3" -> login();
			case "4" -> register();
			default -> {
				System.out.print("'" + result + "' is not valid input, please select a valid option or press 1 for help.\n\n");
				preLogin();
			}

		}
	}

	private void preLoginHelp() {
		System.out.print("""
				Help:
				Please enter into the terminal the number of the option you wish to select then press 'Enter'
				Press '1' to repeat this message
				Press '2' to quit the application
				Press '3' to Login with your existing username and password
				Press '4' to create a new account
				                
				""");

		preLogin();
	}

	private void login() {
		System.out.print("""
				Login:
				Please enter your username:
				""");
		String username = scanner.nextLine();

		System.out.print("\nPlease enter your password:\n");
		String password = scanner.nextLine();

		System.out.print("\n");

		try {
			serverFacade.login(username, password);
			postLogin();
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 401) {
				System.out.print("Please check your username and password and try again.\n");
				preLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n");
				preLogin();
			}
		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			preLogin();
		}
	}

	private void register() {
		System.out.print("""
				Register:
				Please enter a username:
				""");
		String username = scanner.nextLine();

		System.out.print("Please enter a password:\n");
		String password = scanner.nextLine();

		System.out.print("Please enter a valid email address:\n");
		String email = scanner.nextLine();
		System.out.print("\n");

		try {
			serverFacade.register(username, password, email);
			postLogin();
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 403) {
				System.out.print("username or email is already taken. Please choose a different username or use a different email.\n\n");
				preLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n\n");
				preLogin();
			}

		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			preLogin();
		}
	}

	private void postLogin() {
		System.out.print("""
				Please Select an Option
				1. Help
				2. Logout
				3. Create Game
				4. List Games
				5. Join Game
				6. Join Game as Observer
				                
				""");
		String result = scanner.nextLine();

		switch (result) {
			case "1" -> postLoginHelp();
			case "2" -> logout();
			case "3" -> createGame();
			case "4" -> listGames();
			case "5" -> joinGameDialogue();
			case "6" -> joinGameObserver();
			default -> {
				System.out.print("'" + result + "' is not valid input, please select a valid option or press 1 for help\n\n");
				postLogin();
			}
		}

	}

	private void postLoginHelp() {
		System.out.print("""
				Help:
				Please enter into the terminal the number of the option you wish to select then press 'Enter'
				Press '1' to repeat this message
				Press '2' to logout of your account
				Press '3' to create a new chess game
				Press '4' to show a list of all active chess games
				Press '5' to join an active chess game as a player
				Press '6' to join an active chess game as an observer
				                
				""");

		postLogin();
	}

	private void logout() {
		try {
			serverFacade.logout();
			System.out.print("Logged out. Returning to main menu.\n\n");
			preLogin();
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 401) {
				System.out.print("Login expired, please login again\n\n");
				preLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n\n");
				postLogin();
			}
		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			postLogin();
		}
	}

	private void createGame() {
		System.out.print("""
				Create Game:
				Please enter a name for your game:
				""");
		String gameName = scanner.nextLine();

		try {
			int gameID = serverFacade.createGame(gameName);
			System.out.print("Game: " + gameName + " created successfully with id" + gameID + ". Please join the game from the main menu.\n\n");
			postLogin();
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 401) {
				System.out.print("Login expired, please login again\n\n");
				preLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n\n");
				postLogin();
			}
		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			postLogin();
		}
	}

	private void listGames() {
		try {
			System.out.print("Active games:\n");

			fetchGames();
			printActiveGames();
			postLogin();
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 401) {
				System.out.print("Login expired, please login again\n\n");
				preLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n\n");
				postLogin();
			}
		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			postLogin();
		}
	}

	private void fetchGames() throws HTTPResponseException, HTTPConnectionException {
		Set<GameData> gameDataSet = serverFacade.listGames();
		for (GameData gameData : gameDataSet) {
			boolean isInList = false;
			for (GameData gameData1 : games) {
				if (Objects.equals(gameData1.gameID(), gameData.gameID())) {
					isInList = true;
					break;
				}
			}

			if (!isInList) {
				games.add(gameData);
			}
		}
	}

	private void printActiveGames() {
		for (int i = 0; i < games.size(); i++) {
			System.out.print(Integer.valueOf(i + 1).toString() + ": " + games.get(i).gameName() + "\n");
		}
		System.out.print("\n");
	}

	private void joinGameDialogue() {
		try {
			fetchGames();
			printActiveGames();

			System.out.print("""
					Join game:
					Please select which game to join:
					""");
			String gameIndex = scanner.nextLine();
			while ((!gameIndex.matches("\\d+") || Integer.parseInt(gameIndex) > games.size()) && !gameIndex.equals("BACK")) {
				System.out.print("Please enter a valid game index or enter 'BACK' to go back to main menu.\n\n");
				gameIndex = scanner.nextLine();

			}
			if (!gameIndex.equals("BACK")) {
				System.out.print("Please choose which color to join as. Enter 'BLACK' or 'WHITE'.\n");
				String colorSelection = scanner.nextLine();
				while (!colorSelection.equals("BLACK") && !colorSelection.equals("WHITE") && !colorSelection.equals("BACK")) {
					System.out.print("Please choose a valid color. Enter 'BLACK' or 'WHITE'. Enter 'BACK' to go back to main menu.\n\n");
					colorSelection = scanner.nextLine();
				}
				if (!colorSelection.equals("BACK")) {
					joinGame(Integer.parseInt(gameIndex), colorSelection);
				}
			}
			postLogin();
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 401) {
				System.out.print("Login expired, please login again\n\n");
				preLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n\n");
				postLogin();
			}
		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			postLogin();
		}

	}

	private void joinGame(int gameIndex, String teamColor) {
		try {
			serverFacade.joinGame(teamColor, games.get(gameIndex - 1).gameID());
			currentGameID = games.get(gameIndex - 1).gameID();

			awaitError = true;
			awaitBoard = true;
			while (awaitBoard & awaitError) Thread.onSpinWait();
			if (!awaitError) {
				awaitBoard = false;
				postLogin();
			} else {
				awaitError = false;
				inGameMenu();
			}
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 401) {
				System.out.print("Login expired, please login again\n\n");
				preLogin();
			} else if (e.getStatus() == 403) {
				System.out.print("Spot is already taken. Please try a different color or a different game.\n\n");
				postLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n\n");
				postLogin();
			}
		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			postLogin();
		}
	}

	private void joinGameObserver() {
		try {
			fetchGames();
			printActiveGames();

			System.out.print("""
					Join Game as Spectator:
					Please select which game to join:
					""");
			String gameIndex = scanner.nextLine();
			while ((!gameIndex.matches("\\d+") || Integer.parseInt(gameIndex) > games.size()) && !gameIndex.equals("BACK")) {
				System.out.print("Please enter a valid game index or enter 'BACK' to go back to main menu.\n\n");
				gameIndex = scanner.nextLine();
			}

			if (!gameIndex.equals("BACK")) {
				serverFacade.joinGame(null, games.get(Integer.parseInt(gameIndex) - 1).gameID());
			}
			currentGameID = games.get(Integer.parseInt(gameIndex) - 1).gameID();
			postLogin();
		} catch (HTTPResponseException e) {
			if (e.getStatus() == 401) {
				System.out.print("Login expired, please login again\n\n");
				preLogin();
			} else {
				System.out.print("An error occurred, please try again.\n" +
						"Error " + e.getStatus() + ": " + e.getMessage() + "\n\n");
				postLogin();
			}
		} catch (HTTPConnectionException e) {
			System.out.print("Could not establish a connection. Please try again later.\n" +
					"Error: " + e + "\n\n");
			postLogin();
		}
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

			awaitError = true;
			awaitBoard = true;

			while (awaitError & awaitBoard) Thread.onSpinWait();

			if (!awaitError) {
				awaitBoard = false;
			} else {
				awaitError = false;
			}

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
	}

	private void leaveGame() {
		try {
			serverFacade.leaveGame(currentGameID);
			teamColor = null;
			currentGame = null;
			currentGameID = null;
			isSpectator = false;

			System.out.print("Left game, returning to main menu.\n\n");
			postLogin();
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
		boolean isForward = (teamColor == WHITE);
		ChessBoardGraphics.drawChessBoard(currentGame.getBoard(), isForward, null, null);
		awaitBoard = false;
	}

	public void showNotification(NotificationServerMessage notificationServerMessage) {
		System.out.print(notificationServerMessage.getMessage() + "\n");
	}

	private void showError(ErrorServerMessage errorServerMessage) {
		System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + errorServerMessage.getErrorMessage() + EscapeSequences.RESET_ALL + "\n");
		awaitError = false;
	}

}