package comp3350.a15.eventease.logic.exceptions;

public class NewPasswordMissingLowercaseException extends PasswordException {

    public NewPasswordMissingLowercaseException(String message) {
        super(message);
    }
}
