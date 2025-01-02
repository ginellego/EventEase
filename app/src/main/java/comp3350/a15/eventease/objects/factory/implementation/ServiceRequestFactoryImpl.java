package comp3350.a15.eventease.objects.factory.implementation;

import java.time.LocalDate;

import javax.inject.Inject;

import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;

public class ServiceRequestFactoryImpl implements IServiceRequestFactory {
    @Inject
    public ServiceRequestFactoryImpl() {
    }

    @Override
    public ServiceRequest create(Event associatedEvent, int vendorID, String serviceType, LocalDate requestDate, long budget, ServiceRequest.ServiceStatus status) {
        return new ServiceRequest(associatedEvent, vendorID, serviceType, requestDate, budget, status);
    }
}
