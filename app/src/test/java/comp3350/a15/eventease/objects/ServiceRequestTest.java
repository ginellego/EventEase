package comp3350.a15.eventease.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class ServiceRequestTest {
    @Test
    public void testServiceRequest() {

        //Arrange
        String eventName = "Event Name";
        String eventDescription = "Event Description";
        String eventLocation = "Event Location";
        LocalDate eventDate = LocalDate.parse("2024-01-01");
        LocalTime eventTime = LocalTime.parse("12:00");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int plannerID = 1;
        Event event = new Event(plannerID, eventName, eventDescription, eventLocation, eventDate, eventTime, timestamp);
        int vendorID = 1;
        String serviceType = "Cake";
        LocalDate deadline = eventDate.minusDays(1);
        long budget = 100000;
        ServiceRequest.ServiceStatus serviceStatus = ServiceRequest.ServiceStatus.NEW;


        //Act
        ServiceRequest serviceRequest = new ServiceRequest(event, vendorID, serviceType, deadline, budget, serviceStatus);

        //Assert
        assertNotNull("The request should not be null", serviceRequest);
        assertEquals("The request does not have the correct event", event, serviceRequest.getAssociatedEvent());
        assertEquals("The request does not have the correct vendor ID", vendorID, serviceRequest.getVendorID());
        assertEquals("The request does not have the correct serviceType", serviceType, serviceRequest.getServiceType());
        assertEquals("The request does not have the correct deadline date", deadline, serviceRequest.getDeadline());
        assertEquals("The request does not have the correct budget", budget, serviceRequest.getBudget());
        assertEquals("The request does not have a new service status", serviceStatus, serviceRequest.getServiceStatus());
    }
}
