package model.request;

public record JoinGameRequest(String playerColor, int gameID) implements Request {
}
