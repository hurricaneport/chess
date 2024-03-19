package ui;

import api.HTTPException;
import api.ServerFacade;
import chess.ChessGame;
import model.GameData;
import model.request.JoinGameRequest;
import model.request.LoginRequest;

import java.util.*;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade();
    private final ArrayList<GameData> games = new ArrayList<>();
    public void run() {
        System.out.print("Chess 2024\n");
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
            serverFacade.login(new LoginRequest(username, password));
            postLogin();
        }
        catch (HTTPException e) {
            if (e.getStatus() == 401) {
                System.out.print("Please check your username and password and try again.\n");
                preLogin();
            }
            else {
                System.out.print("An error occurred, please try again.\n" +
                        "Error " + e.getStatus() + ": " + e.getMessage() + "\n");
                preLogin();
            }
        }
    }

    private void register() {

    }

    private void postLogin() {
        System.out.print("""
                Please Select an Option
                1. Help
                2. Logout
                3. List Games
                4. Join Game
                5. Join Game as Observer
                
                """);
        String result = scanner.nextLine();

        switch (result) {
            case "1" -> postLoginHelp();
            case "2" -> logout();
            case "3" -> listGames();
            case "4" -> joinGameDialogue();
            case "5" -> joinGameObserver();
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
                Press '3' to show a list of all active chess games
                Press '4' to join an active chess game as a player
                Press '5' to join an active chess game as an observer
                
                """);

        postLogin();
    }

    private void logout() {
        try {
            serverFacade.logout();
            System.out.print("Logged out. Returning to main menu.\n");
            preLogin();
        }
        catch (HTTPException e) {
            if (e.getStatus() == 401) {
                System.out.print("Could not log out because log-in is expired. Please log in again.\n");
            }
            else {
                System.out.print("An error occurred, please try again.\n" +
                        "Error " + e.getStatus() + ": " + e.getMessage() + "\n");
                postLogin();
            }
        }
    }

    private void listGames() {
        try {
            System.out.print("Active games:\n");

            fetchGames();
            printActiveGames();
            postLogin();
        }
        catch (HTTPException e) {
            if (e.getStatus() == 401) {
                System.out.print("Login expired, please login again\n");
                preLogin();
            }
            else {
                System.out.print("An error occurred, please try again.\n" +
                        "Error " + e.getStatus() + ": " + e.getMessage() + "\n");
                postLogin();
            }
        }
    }

    private void fetchGames() throws HTTPException {
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

            System.out.print("Please select which game to join\n");
            String gameIndex = scanner.nextLine();
            while ((!gameIndex.matches("\\d+") || Integer.parseInt(gameIndex) > games.size()) && !gameIndex.equals("BACK")) {
                System.out.print("Please enter a valid game index or enter 'BACK' to go back to main menu.\n");
                gameIndex = scanner.nextLine();

            }
            if (!gameIndex.equals("BACK")) {
                System.out.print("Please choose which color to join as. Enter 'BLACK' or 'WHITE'.\n");
                String colorSelection = scanner.nextLine();
                while (!colorSelection.equals("BLACK") && !colorSelection.equals("WHITE") && !colorSelection.equals("BACK")) {
                    System.out.print("Please choose a valid color. Enter 'BLACK' or 'WHITE'. Enter 'BACK' to go back to main menu.\n");
                    colorSelection = scanner.nextLine();
                }
                if (!colorSelection.equals("BACK")) {
                    joinGame(Integer.parseInt(gameIndex), colorSelection);
                }
            }
            postLogin();
        }
        catch (HTTPException e) {
            if (e.getStatus() == 401) {
                System.out.print("Login expired, please login again\n");
                preLogin();
            }
            else {
                System.out.print("An error occurred, please try again.\n" +
                        "Error " + e.getStatus() + ": " + e.getMessage() + "\n");
                postLogin();
            }
        }


    }

    private void joinGame(int gameIndex, String teamColor) {
        try {
            serverFacade.joinGame(new JoinGameRequest(teamColor, games.get(gameIndex - 1).gameID()));
            ChessBoardGraphics.drawChessBoard(games.get(gameIndex - 1).game().getBoard(), true);
            ChessBoardGraphics.drawChessBoard(games.get(gameIndex - 1).game().getBoard(), false);
        } catch (HTTPException e) {
            if (e.getStatus() == 401) {
                System.out.print("Login expired, please login again\n");
                preLogin();
            }
            else if (e.getStatus() == 403) {
                System.out.print("Spot is already taken. Please try a different color or a different gae.\n");
            }
            else {
                System.out.print("An error occurred, please try again.\n" +
                        "Error " + e.getStatus() + ": " + e.getMessage() + "\n");
                postLogin();
            }
        }
    }

    private void joinGameObserver() {
        try {
            fetchGames();
            printActiveGames();

            System.out.print("Please select which game to join\n");
            String gameIndex = scanner.nextLine();
            while ((!gameIndex.matches("\\d+") || Integer.parseInt(gameIndex) > games.size()) && !gameIndex.equals("BACK")) {
                System.out.print("Please enter a valid game index or enter 'BACK' to go back to main menu.\n");
                gameIndex = scanner.nextLine();
            }

            if (!gameIndex.equals("BACK")) {
                serverFacade.joinGame(new JoinGameRequest(null, games.get(Integer.parseInt(gameIndex) - 1).gameID()));
                ChessBoardGraphics.drawChessBoard(games.get(Integer.parseInt(gameIndex) - 1).game().getBoard(), true);
                ChessBoardGraphics.drawChessBoard(games.get(Integer.parseInt(gameIndex) - 1).game().getBoard(), false);
            }
        }
        catch (HTTPException e) {
            if (e.getStatus() == 401) {
                System.out.print("Login expired, please login again\n");
                preLogin();
            }
            else {
                System.out.print("An error occurred, please try again.\n" +
                        "Error " + e.getStatus() + ": " + e.getMessage() + "\n");
                postLogin();
            }
        }
    }


}
