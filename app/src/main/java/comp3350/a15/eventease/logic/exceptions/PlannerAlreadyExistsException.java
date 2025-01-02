package comp3350.a15.eventease.logic.exceptions;

public class PlannerAlreadyExistsException extends RuntimeException{
    public PlannerAlreadyExistsException(String message){super(message);}
    @Override
    public String getMessage() {
        return "Duplicate planner found. Planner already exists.";
    }
}
