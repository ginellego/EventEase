package comp3350.a15.eventease.logic.exceptions;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Duplicate event found. Event already exists.";
    }
}
