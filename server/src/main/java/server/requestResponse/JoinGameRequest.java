package server.requestResponse;

public record JoinGameRequest(String playerColor, int gameID) implements Request {
}
