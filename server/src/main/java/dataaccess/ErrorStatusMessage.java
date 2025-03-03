package dataaccess;

public class ErrorStatusMessage {
    private String status;
    private String message;

    public ErrorStatusMessage(String status, String message) {
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
