package dataAccess;

import model.GameData;

import java.util.List;

public class DBGameDAO implements GameDAO{
    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public List<GameData> getGames() {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {

    }
}
