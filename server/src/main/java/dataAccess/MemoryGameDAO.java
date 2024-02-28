package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private static final MemoryGameDAO gameDAO = new MemoryGameDAO();


    private List<GameData> gameTable = new ArrayList<>();

    public static MemoryGameDAO getGameDAO() {
        return gameDAO;
    }
    @Override
    public void clear() {
        gameTable = new ArrayList<>();
    }

    @Override
    public boolean isEmpty() {
        return gameTable.isEmpty();
    }

    @Override
    public List<GameData> getGames() {
        return gameTable;
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData gameData : gameTable) {
            if (gameData.gameID() == gameID) {
                return gameData;
            }
        }

        return null;
    }

    @Override
    public void createGame(GameData gameData) {
        gameTable.add(gameData);
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        GameData oldGameData = getGame(gameID);
        if (oldGameData == null) {
            throw new DataAccessException("Game does not exist");
        }

        gameTable.remove(oldGameData);
        gameTable.add(gameData);
    }
}
