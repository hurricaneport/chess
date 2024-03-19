package api;

import chess.ChessGame;
import model.GameData;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResponse;
import model.response.RegisterResponse;

import java.util.HashSet;
import java.util.Set;

public class ServerFacade {
    public void login(LoginRequest loginRequest) throws HTTPException {

    }

    public RegisterResponse register(RegisterRequest registerRequest) throws HTTPException {
        return null;
    }

    public void logout() throws HTTPException {

    }

    public Set<GameData> listGames() throws HTTPException {
        HashSet<GameData> games = new HashSet<>();
        games.add(new GameData(1, null, null, "game", new ChessGame()));
        games.add(new GameData(2, null, null, "game2", new ChessGame()));
        return games;
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws HTTPException {

    }
}
