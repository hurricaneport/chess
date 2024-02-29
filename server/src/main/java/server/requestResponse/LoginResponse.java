package server.requestResponse;

public record LoginResponse(String username, String authToken) implements Response {
}
