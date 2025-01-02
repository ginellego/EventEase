package comp3350.a15.eventease.logic.exceptions;

public class NewPasswordMissingSpecialCharException extends PasswordException {
    public NewPasswordMissingSpecialCharException(String message) {
        super(message);
    }
}
