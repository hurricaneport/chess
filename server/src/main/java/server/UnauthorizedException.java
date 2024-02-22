package server;

public class UnauthorizedException extends Exception {
    private static final int status = 401;

    public UnauthorizedException(String message) {
        super(message);
    }

    public static int getStatus() {
        return status;
    }
}
