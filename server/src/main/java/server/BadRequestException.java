package server;

public class BadRequestException extends Exception {
    private static final int status = 400;

    public BadRequestException(String message) {
        super(message);
    }

    public static int getStatus() {
        return status;
    }
}
