package serverTests.dataAccessTests;

import chess.ChessGame;
import dataAccess.sql.DBGameDAO;
import dataAccess.GameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class GameDAOTests {
    final GameDAO gameDAO = DBGameDAO.getGameDAO();

    @BeforeEach
    public void clearDatabase() throws Exception{
        gameDAO.clear();
    }

    @Test
    @DisplayName("Clear Game Database")
    public void clearGameDatabase() throws Exception {
        gameDAO.clear();
    }

    @Test
    @DisplayName("Add a game")
    public void createGame() throws Exception {
        int gameID = gameDAO.createGame(new GameData(null, null, null, "gameName", new ChessGame()));
    }

    @Test
    @DisplayName("Retrieve a game with existing ID")
    public void getGame() throws Exception {
        int gameID = gameDAO.createGame(new GameData(null, null, null, "game", new ChessGame()));
        GameData expectedGameData = new GameData(gameID, null, null, "game", new ChessGame());
        Assertions.assertEquals(expectedGameData, gameDAO.getGame(gameID));
    }

    @Test
    @DisplayName("Update a game")
    public void updateGame() throws Exception {
        int gameID = gameDAO.createGame(new GameData(null, null, null, "game", new ChessGame()));
        GameData updateGame = new GameData(gameID, "John", null, "game", new ChessGame());
        gameDAO.updateGame(gameID, updateGame);

        Assertions.assertEquals(updateGame, gameDAO.getGame(gameID));
    }

    @Test
    @DisplayName("Retrieve all games")
    public void getGames() throws Exception {
        GameData game1 = new GameData(null, null, null, "game1", new ChessGame());
        GameData game2 = new GameData(null, null, null, "game2", new ChessGame());
        GameData game3 = new GameData(null, null, null, "game3", new ChessGame());
        GameData game4 = new GameData(null, null, null, "game4", new ChessGame());

        int gameID1 = gameDAO.createGame(game1);
        GameData expectedGame1 = new GameData(gameID1, null, null, "game1", new ChessGame());
        int gameID2 = gameDAO.createGame(game2);
        GameData expectedGame2 =  new GameData(gameID2, null, null, "game2", new ChessGame());
        int gameID3 = gameDAO.createGame(game3);
        GameData expectedGame3 = new GameData(gameID3, null, null, "game3", new ChessGame());
        int gameID4 = gameDAO.createGame(game4);
        GameData expectedGame4 = new GameData(gameID4, null, null, "game4", new ChessGame());

        Set<GameData> games = gameDAO.getGames();
        Set<GameData> expectedGames = new HashSet<>();
        expectedGames.add(expectedGame1);
        expectedGames.add(expectedGame2);
        expectedGames.add(expectedGame3);
        expectedGames.add(expectedGame4);

        Assertions.assertEquals(expectedGames, games);
    }

    @Test
    @DisplayName("Test isEmpty on full database")
    public void isEmptyFull() throws Exception {
        GameData game1 = new GameData(null, null, null, "game1", new ChessGame());
        GameData game2 = new GameData(null, null, null, "game2", new ChessGame());
        GameData game3 = new GameData(null, null, null, "game3", new ChessGame());
        GameData game4 = new GameData(null, null, null, "game4", new ChessGame());

        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameDAO.createGame(game3);
        gameDAO.createGame(game4);

        Assertions.assertFalse(gameDAO.isEmpty());


    }

    @Test
    @DisplayName("Test isEmpty on empty database")
    public void isEmptyEmpty() throws Exception {
        GameData game1 = new GameData(null, null, null, "game1", new ChessGame());
        GameData game2 = new GameData(null, null, null, "game2", new ChessGame());
        GameData game3 = new GameData(null, null, null, "game3", new ChessGame());
        GameData game4 = new GameData(null, null, null, "game4", new ChessGame());

        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameDAO.createGame(game3);
        gameDAO.createGame(game4);

        gameDAO.clear();

        Assertions.assertTrue(gameDAO.isEmpty());


    }
}
