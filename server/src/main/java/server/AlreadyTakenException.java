package server;

public class AlreadyTakenException extends Exception {
    private static final int status = 403;
    public AlreadyTakenException(String message) {
        super(message);
    }

    public static int getStatus() {
        return status;
    }
}
