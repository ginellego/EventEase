package comp3350.a15.eventease.logic.exceptions;

public class PlannerNotAddedException extends RuntimeException{
    public PlannerNotAddedException(String message){super(message); }
    @Override
    public String getMessage() {
        return "Error creating event. Please try again.";
    }
}
