package api;

import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.ListGamesResponse;
import model.response.LoginResponse;
import model.response.RegisterResponse;
import model.response.Response;

public class ServerFacade {
    public LoginResponse login(LoginRequest loginRequest) throws HTTPException {
        return null;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws HTTPException {
        return null;
    }

    public void logout() throws HTTPException {

    }

    public ListGamesResponse listGames() throws HTTPException {
        return null;
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws HTTPException {

    }
}
