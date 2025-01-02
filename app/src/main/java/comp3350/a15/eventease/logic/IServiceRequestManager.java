package comp3350.a15.eventease.logic;

import java.time.LocalDate;
import java.util.List;

import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;

public interface IServiceRequestManager {
    List<ServiceRequest> getAllRequests(int vendorId);

    List<ServiceRequest> getAllRequestsOfEvent(int eventId);

    boolean doesRequestExist(ServiceRequest request);

    ServiceRequest addNewRequest(Event associated, int vendorID, String serviceType,
                                 LocalDate requestDate, long budget);

    ServiceRequest addRequest(ServiceRequest request);

    void deleteRequest(ServiceRequest request);

    ServiceRequest getRequest(int requestId, int vendorId);

    void setServiceStatus(int id, ServiceRequest.ServiceStatus serviceStatus);
}
