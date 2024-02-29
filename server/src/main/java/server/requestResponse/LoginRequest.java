package server.requestResponse;

public record LoginRequest(String username, String password) implements Request {
}
