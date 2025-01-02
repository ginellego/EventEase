package comp3350.a15.eventease.logic.exceptions;

public class EventNotDeletedException extends RuntimeException {
    public EventNotDeletedException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Event could not be deleted";
    }
}
