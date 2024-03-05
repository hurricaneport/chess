package dataAccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    /**
     * Clears Games Table of DB
     */
    void clear() throws DataAccessException;

    /**
     * @return true if auth table is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * @return returns list of all currently stored games
     */
    List<GameData> getGames();

    GameData getGame(int gameID);

    /**
     * Creates a new game in the database
     * @param gameData GameData of game to be added
     * @return returns game_id
     */
    int createGame(GameData gameData) throws DataAccessException;

    void updateGame(int gameID, GameData gameData) throws DataAccessException;
}
