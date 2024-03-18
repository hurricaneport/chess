package ui;

import api.HTTPException;
import api.ServerFacade;
import model.request.LoginRequest;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade();
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
            case "1" -> {
                preLoginHelp();
            }
            case "2" -> {

            }
            case "3" -> {
                login();
            }
            case "4" -> {
                register();
            }
            default -> {
                System.out.print(result + "is not valid input, please select a valid option\n\n");
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
                login();
            }
            else {
                System.out.print("An error occurred, please try again.\n" +
                        "Error " + e.getStatus() + ": " + e.getMessage());
                login();
            }
        }
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
            case "1" -> {

            }
            case "2" -> {

            }
            case "3" -> {

            }
            case "4" -> {

            }
            case "5" -> {

            }
            default -> {
                System.out.print(result + "is not valid input, please select a valid option\n\n");
                postLogin();
            }
        }

    }

    private void register() {

    }
}
