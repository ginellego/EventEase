package comp3350.a15.eventease.logic.implementation;

import java.time.LocalDate;
import java.util.List;

import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.logic.exceptions.ServiceRequestAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.ServiceRequestNotFoundException;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

public abstract class BaseServiceRequestManager implements IServiceRequestManager {

    protected final IServiceRequestPersistence serviceRequestPersistence;
    private final IServiceRequestFactory serviceRequestFactory;


    public BaseServiceRequestManager(IServiceRequestPersistence serviceRequestPersistence,
                                     IServiceRequestFactory serviceRequestFactory) {
        this.serviceRequestPersistence = serviceRequestPersistence;
        this.serviceRequestFactory = serviceRequestFactory;
    }

    @Override
    public boolean doesRequestExist(ServiceRequest request) {
        return serviceRequestPersistence.doesRequestExist(request);
    }

    @Override
    public ServiceRequest addNewRequest(Event associatedEvent, int vendorID, String serviceType,
                                        LocalDate requestDate, long budget) {
        ServiceRequest addedRequest = makeRequest(associatedEvent, vendorID, serviceType,
                requestDate, budget);
        return addRequest(addedRequest);
    }

    @Override
    public List<ServiceRequest> getAllRequestsOfEvent(int eventId) {
        return serviceRequestPersistence.getAllRequestsOfEvent(eventId);
    }

    @Override
    public ServiceRequest addRequest(ServiceRequest request) {
        if (!doesRequestExist(request)) {
            serviceRequestPersistence.addNewRequest(request);
            return request;
        } else {
            throw new ServiceRequestAlreadyExistsException();
        }
    }

    @Override
    public void deleteRequest(ServiceRequest request) {
        serviceRequestPersistence.deleteRequest(request);
    }

    @Override
    public ServiceRequest getRequest(int requestId, int vendorId) {
        try {
            return serviceRequestPersistence.getRequest(requestId, vendorId);
        } catch (PersistenceException e) {
            throw new ServiceRequestNotFoundException();
        }
    }

    @Override
    public void setServiceStatus(int id, ServiceRequest.ServiceStatus serviceStatus) {
        serviceRequestPersistence.setServiceStatus(id, serviceStatus);
    }

    protected ServiceRequest makeRequest(Event associatedEvent, int vendorID, String serviceType,
                                         LocalDate requestDate, long budget) {
        return serviceRequestFactory.create(associatedEvent, vendorID, serviceType,
                requestDate, budget, null);//null serviceStatus marks it as NEW
    }
}
