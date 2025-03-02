package dataaccess;

public class ErrorBadRequest {
    private String status;
    private String message;

    public ErrorBadRequest(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
