package comp3350.a15.eventease.logic.exceptions;

public class EventNotAddedException extends RuntimeException {
    public EventNotAddedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Error creating event. Please try again.";
    }
}
