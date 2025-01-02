package comp3350.a15.eventease.logic.exceptions;

public class NewPasswordMissingUppercaseException extends PasswordException {
    public NewPasswordMissingUppercaseException(String message) {
        super(message);
    }
}
