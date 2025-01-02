package comp3350.a15.eventease.logic.exceptions;

import androidx.annotation.Nullable;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    @Nullable
    @Override
    public String getMessage() {
        return "User was not found";
    }
}
