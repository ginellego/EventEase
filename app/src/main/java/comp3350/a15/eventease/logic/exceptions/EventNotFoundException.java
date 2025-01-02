package comp3350.a15.eventease.logic.exceptions;

import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, PersistenceException e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return "Event could not be found.";
    }
}
