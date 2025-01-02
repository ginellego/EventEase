package comp3350.a15.eventease.persistence_stub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;

public class ServiceRequestPersistenceStub implements IServiceRequestPersistence {
    private final ArrayList<ServiceRequest> serviceRequests;

    @Inject
    public ServiceRequestPersistenceStub() {
        serviceRequests = new ArrayList<>();
    }

    @Override
    public List<ServiceRequest> getAllRequests(int vendorId) {
        return Collections.unmodifiableList(serviceRequests);
    }

    @Override
    public void addNewRequest(ServiceRequest newEvent) {
        if (newEvent != null && !serviceRequests.contains(newEvent)) {
            serviceRequests.add(newEvent);

        }
    }

    @Override
    public void deleteRequest(ServiceRequest event) {
        serviceRequests.remove(event);
    }

    @Override
    public boolean doesRequestExist(ServiceRequest request) {
        return serviceRequests.contains(request);
    }

    @Override
    public ServiceRequest getRequest(int position, int vendorId) {
        return serviceRequests.get(position);
    }

    @Override
    public void setServiceStatus(int position, ServiceRequest.ServiceStatus serviceStatus) {
        serviceRequests.get(position).setServiceStatus(serviceStatus);
    }

    @Override
    public List<ServiceRequest> getAllRequestsOfEvent(int eventId) {
        return serviceRequests.stream().filter(request -> request.getAssociatedEvent().getId() == eventId).collect(Collectors.toList());
    }
}
