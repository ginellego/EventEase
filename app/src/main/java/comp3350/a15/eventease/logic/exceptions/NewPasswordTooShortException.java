package comp3350.a15.eventease.logic.exceptions;

public class NewPasswordTooShortException extends PasswordException {
    public NewPasswordTooShortException(String message) {
        super(message);
    }
}
