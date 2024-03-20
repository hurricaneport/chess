package api;

public class HTTPResponseException extends Exception {
    private final int status;
    HTTPResponseException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
