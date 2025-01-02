package comp3350.a15.eventease.logic.exceptions;

public class ServiceRequestNotFoundException extends ServiceRequestException {
    @Override
    public String getMessage() {
        return "The request was not found";
    }
}
