package comp3350.a15.eventease.logic.exceptions;

public class EventNotEditedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Error editing event. Please try again.";
    }
}
