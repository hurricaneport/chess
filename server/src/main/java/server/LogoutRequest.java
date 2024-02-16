package server;

public record LogoutRequest(String username, String authToken) implements Request {
}
