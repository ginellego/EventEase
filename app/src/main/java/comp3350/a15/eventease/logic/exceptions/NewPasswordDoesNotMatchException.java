package comp3350.a15.eventease.logic.exceptions;

public class NewPasswordDoesNotMatchException extends PasswordException{
    public NewPasswordDoesNotMatchException(String message) {
        super(message);
    }
}
