package service;

import chess.ChessGame;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import server.*;
import dataAccess.MemoryGameDAO;

public class GameService extends Service {
    private static final GameService staticGameService = new GameService();

    private final GameDAO gameDAO = MemoryGameDAO.getGameDAO();

    private static int currentGameID = 0;

    /**
     * @return Returns a static instance of GameService
     */
    public static GameService getInstance() {
        return staticGameService;
    }

    public Response listGames(String authToken) throws ServerErrorException, UnauthorizedException {
        AuthData authData = authorize(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new ListGamesResponse(gameDAO.getGames());
    }

    public Response createGame(String authToken, CreateGameRequest createGameRequest) throws ServerErrorException, UnauthorizedException, BadRequestException {
        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authorize(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }


        gameDAO.createGame(new GameData(currentGameID, null, null, createGameRequest.gameName(), new ChessGame()));
        CreateGameResponse createGameResponse = new CreateGameResponse(currentGameID);
        currentGameID++;

        return createGameResponse;
    }
}
