package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import dataAccess.memory.MemoryGameDAO;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.ServerErrorException;
import service.exceptions.UnauthorizedException;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.response.CreateGameResponse;
import service.response.ListGamesResponse;
import service.response.Response;

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
        try {
            return new ListGamesResponse(gameDAO.getGames());
        } catch (DataAccessException e) {
            throw new ServerErrorException("Error: " + e);
        }
    }

    public Response createGame(String authToken, CreateGameRequest createGameRequest) throws ServerErrorException, UnauthorizedException, BadRequestException {
        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authorize(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }


        try {
            gameDAO.createGame(new GameData(currentGameID, null, null, createGameRequest.gameName(), new ChessGame()));
        } catch (DataAccessException e) {
            throw new ServerErrorException("Error: " + e);
        }
        CreateGameResponse createGameResponse = new CreateGameResponse(currentGameID);
        currentGameID++;

        return createGameResponse;
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws BadRequestException, ServerErrorException, UnauthorizedException, AlreadyTakenException {
        GameData gameData = null;
        try {
            gameData = gameDAO.getGame(joinGameRequest.gameID());
        } catch (DataAccessException e) {
            throw new ServerErrorException("Error: " + e);
        }

        if (gameData == null) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData authData = authorize(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        if (!"WHITE".equals(joinGameRequest.playerColor()) && !"BLACK".equals(joinGameRequest.playerColor()) && joinGameRequest.playerColor() != null) {
            throw new BadRequestException("Error: bad request");
        }
        else if ("WHITE".equals(joinGameRequest.playerColor())) {
            if (whiteUsername == null || whiteUsername.equals(authData.username())) {
                whiteUsername = authData.username();
            }
            else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
        else if ("BLACK".equals(joinGameRequest.playerColor())) {
            if (blackUsername == null || blackUsername.equals(authData.username())) {
                blackUsername = authData.username();
            }
            else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }

        GameData newGameData = new GameData(gameData.gameID(), whiteUsername, blackUsername, gameData.gameName(), gameData.game());
        try {
            gameDAO.updateGame(gameData.gameID(), newGameData);
        } catch (DataAccessException e) {
            throw new ServerErrorException("Error: database error");
        }
    }
}
