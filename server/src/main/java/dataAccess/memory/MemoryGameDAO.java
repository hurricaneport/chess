package dataAccess.memory;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {
    private static final MemoryGameDAO gameDAO = new MemoryGameDAO();


    private Set<GameData> gameTable = new HashSet<>();

    public static MemoryGameDAO getGameDAO() {
        return gameDAO;
    }
    @Override
    public void clear() {
        gameTable = new HashSet<>();
    }

    @Override
    public boolean isEmpty() {
        return gameTable.isEmpty();
    }

    @Override
    public Set<GameData> getGames() {
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
    public int createGame(GameData gameData) {
        gameTable.add(gameData);
        return gameData.gameID();
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
