package server.requestResponse;

public record RegisterResponse(String username, String authToken) implements Response {
}
