package server;

public record ErrorResponse(String message, Integer status) implements Response {
}
