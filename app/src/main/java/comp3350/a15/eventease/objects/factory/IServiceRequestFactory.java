package comp3350.a15.eventease.objects.factory;

import java.time.LocalDate;

import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;
public interface IServiceRequestFactory {
    ServiceRequest create(Event associatedEvent, int vendorID,
                          String serviceType, LocalDate requestDate, long budget,
                          ServiceRequest.ServiceStatus serviceStatus);
}
