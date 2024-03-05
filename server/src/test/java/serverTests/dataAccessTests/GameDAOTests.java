package serverTests.dataAccessTests;

import chess.ChessGame;
import dataAccess.sql.DBGameDAO;
import dataAccess.GameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.DatabaseService;

public class GameDAOTests {
    GameDAO gameDAO = DBGameDAO.getGameDAO();
    DatabaseService databaseService = DatabaseService.getInstance();

    @BeforeEach
    public void clearDatabase() throws Exception{
        databaseService.clear();
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
}
