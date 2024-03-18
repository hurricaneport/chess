package api;

public class HTTPException extends Exception {
    private final int status;
    HTTPException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
