package comp3350.a15.eventease.logic.exceptions;

public class ServiceRequestException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Something went wrong with the request...";
    }
}
