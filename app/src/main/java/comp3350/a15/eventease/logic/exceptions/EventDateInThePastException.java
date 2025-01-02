package comp3350.a15.eventease.logic.exceptions;

public class EventDateInThePastException extends IllegalArgumentException {
    public EventDateInThePastException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Event date cannot be in the past";
    }
}
