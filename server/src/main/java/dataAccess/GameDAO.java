package dataAccess;

import model.GameData;

import java.util.Set;

public interface GameDAO {
	/**
	 * Clears Games Table of DB
	 */
	void clear() throws DataAccessException;

	/**
	 * @return true if auth table is empty, false otherwise
	 */
	boolean isEmpty() throws DataAccessException;

	/**
	 * @return returns list of all currently stored games
	 */
	Set<GameData> getGames() throws DataAccessException;

	/**
	 * Gets GameData for a given ID
	 *
	 * @param gameID gameID to be searched
	 * @return GameData of given ID or null if no game was found
	 * @throws DataAccessException for database errors.
	 */

	GameData getGame(int gameID) throws DataAccessException;

	/**
	 * Creates a new game in the database
	 *
	 * @param gameData GameData of game to be added
	 * @return returns game_id
	 */
	int createGame(GameData gameData) throws DataAccessException;

	void updateGame(int gameID, GameData gameData) throws DataAccessException;
}
