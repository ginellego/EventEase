package comp3350.a15.eventease.presentation.enums;

public enum RequestStatusCode {
    UNRESOLVED(0),

    ACCEPTED(1),

    REJECTED(2);

    private final int statusCode;

    RequestStatusCode(int i) {
        this.statusCode = i;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
