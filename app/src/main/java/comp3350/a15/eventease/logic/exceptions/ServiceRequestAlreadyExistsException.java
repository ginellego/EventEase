package comp3350.a15.eventease.logic.exceptions;

public class ServiceRequestAlreadyExistsException extends ServiceRequestException {
    @Override
    public String getMessage() {
        return "This request has already been sent to the vendor";
    }
}
