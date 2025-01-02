package comp3350.a15.eventease.logic.exceptions;

import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

public class PlannerNotFoundException extends RuntimeException{
    public PlannerNotFoundException(String message) {
        super(message);
    }

    public PlannerNotFoundException(String message, PersistenceException e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return "Planner could not be found.";
    }
}
