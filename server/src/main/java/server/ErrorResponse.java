package server;

public record ErrorResponse(String message, Integer status) implements Response {
    public static ErrorResponse stripStatus(ErrorResponse errorResponse) {
        return new ErrorResponse(errorResponse.message(), null);
    }
}
