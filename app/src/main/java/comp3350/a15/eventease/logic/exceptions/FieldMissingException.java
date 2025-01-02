package comp3350.a15.eventease.logic.exceptions;

public class FieldMissingException extends RuntimeException {
    public FieldMissingException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Please fill in all the fields";
    }
}
