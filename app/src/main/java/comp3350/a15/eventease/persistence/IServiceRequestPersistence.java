package comp3350.a15.eventease.persistence;

import java.util.List;

import comp3350.a15.eventease.objects.ServiceRequest;

public interface IServiceRequestPersistence {
    List<ServiceRequest> getAllRequests(int vendorId);

    void addNewRequest(ServiceRequest newEvent);


    void deleteRequest(ServiceRequest event);

    boolean doesRequestExist(ServiceRequest request);

    ServiceRequest getRequest(int requestID, int vendorId);

    void setServiceStatus(int id, ServiceRequest.ServiceStatus serviceStatus);

    List<ServiceRequest> getAllRequestsOfEvent(int eventId);
}
