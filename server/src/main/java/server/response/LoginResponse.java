package server.response;

public record LoginResponse(String username, String authToken) implements Response {
}
