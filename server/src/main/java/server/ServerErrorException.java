package server;

public class ServerErrorException extends Exception {
    private static final int status = 500;

    public ServerErrorException(String message) {
        super(message);
    }

    public static int getStatus() {
        return status;
    }
}
