package comp3350.a15.eventease.logic.exceptions;

public class CantModifyPlannerException extends RuntimeException{
    public CantModifyPlannerException(final Exception cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "Cannot modify the planner.";
    }
}
