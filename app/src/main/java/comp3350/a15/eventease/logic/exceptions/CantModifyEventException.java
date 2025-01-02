package comp3350.a15.eventease.logic.exceptions;

public class CantModifyEventException extends RuntimeException {
    public CantModifyEventException(final Exception cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "Cannot modify the event because a service request has been sent out for it.";
    }
}
