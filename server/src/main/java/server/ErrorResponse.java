package server;

public record ErrorResponse(String message, int status) implements Response {
}
