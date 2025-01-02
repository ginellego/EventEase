package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import comp3350.a15.eventease.logic.implementation.AcceptedServiceRequestManager;
import comp3350.a15.eventease.logic.implementation.RejectedServiceRequestManager;
import comp3350.a15.eventease.logic.implementation.UnresolvedServiceRequestManager;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.persistence_stub.ServiceRequestPersistenceStub;


@RunWith(MockitoJUnitRunner.class)
public class BaseServiceRequestManagerTest {
    private static final Timestamp TIME_STAMP_ONE = new Timestamp(System.currentTimeMillis());
    private static final Timestamp TIME_STAMP_TWO = new Timestamp(System.currentTimeMillis());
    private static final String EVENT1_NAME_STRING = "Test Event1";
    private static final String EVENT2_NAME_STRING = "Test Event2";
    private static final String EVENT_DESCRIPTION_STRING = "Test Description";
    private static final String EVENT_LOCATION_STRING = "Test Location";
    private static final CharSequence EVENT_DATE = LocalDate.now().plusDays(1).toString();
    private static final LocalDate PARSED_DATE = LocalDate.parse(EVENT_DATE);
    private static final LocalDate PARSED_DATE_SERVICE = LocalDate.parse(EVENT_DATE);
    private static final CharSequence EVENT_TIME = "00:00";
    private static final LocalTime PARSED_TIME = LocalTime.parse(EVENT_TIME);
    private static final int PLANNER_ID = 1;
    private static final Event EVENT1 = new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
            PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE);
    private static final int PLANNER_ID_2 = 2;
    private static final Event EVENT2 = new Event(PLANNER_ID_2, EVENT2_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
            PARSED_DATE, PARSED_TIME, TIME_STAMP_TWO);
    private static final int VENDOR_ID = 1;
    private static final int VENDOR_ID_2 = 2;
    private static final String SERVICE_TYPE_STRING = "CAKE";
    private static final long BUDGET = 1000;
    private static final long BUDGET2 = 15000;
    private static final ServiceRequest.ServiceStatus SERVICE_STATUS = ServiceRequest.ServiceStatus.NEW;
    private static IServiceRequestManager requestManager;
    @Mock
    private
    IServiceRequestFactory serviceRequestFactory;

    @Before
    public void testSetup() {
        requestManager = new RejectedServiceRequestManager(new ServiceRequestPersistenceStub(), serviceRequestFactory);
        requestManager.getAllRequests(VENDOR_ID);
        requestManager = new AcceptedServiceRequestManager(new ServiceRequestPersistenceStub(), serviceRequestFactory);
        requestManager.getAllRequests(VENDOR_ID);

        requestManager = new UnresolvedServiceRequestManager(new ServiceRequestPersistenceStub(), serviceRequestFactory);
        when(serviceRequestFactory.create(eq(EVENT1), eq(VENDOR_ID), eq(SERVICE_TYPE_STRING), eq(PARSED_DATE_SERVICE), eq(BUDGET), any())).thenReturn(
                new ServiceRequest(EVENT1, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET, SERVICE_STATUS));

        when(serviceRequestFactory.create(eq(EVENT2), eq(VENDOR_ID_2), eq(SERVICE_TYPE_STRING), eq(PARSED_DATE_SERVICE), eq(BUDGET2), any())).thenReturn(
                new ServiceRequest(EVENT2, VENDOR_ID_2, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET2, SERVICE_STATUS));
    }

    @Test
    public void testAddServiceRequest_success() {
        //Arrange
        ServiceRequest request = new ServiceRequest(EVENT1, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET, SERVICE_STATUS);

        //Act
        ServiceRequest request2 = requestManager.addRequest(request);

        //Assert
        List<ServiceRequest> finalEvents = requestManager.getAllRequests(VENDOR_ID);
        assertEquals("The request was not added successfully", 1, finalEvents.size());
        assertTrue(finalEvents.contains(request2));
        assertEquals("The request added was not equal to what is sent", request, requestManager.getRequest(0, VENDOR_ID));
    }


    @Test
    public void testAddNewServiceNewRequest_success() {
        //Act
        ServiceRequest request = requestManager.addNewRequest(EVENT2, VENDOR_ID_2, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET2);

        //Assert
        List<ServiceRequest> finalEvents = requestManager.getAllRequests(VENDOR_ID);
        assertEquals("New request was not added successfully", 1, finalEvents.size());
        assertTrue(finalEvents.contains(request));
        assertEquals("The request added was not equal to what is sent", request, requestManager.getRequest(0, VENDOR_ID));
    }


    @Test
    public void testGetRequest_success() {
        //Arrange
        ServiceRequest request1 = requestManager.addNewRequest(EVENT1, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);
        ServiceRequest request2 = requestManager.addNewRequest(EVENT2, VENDOR_ID_2, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET2);

        //Act
        ServiceRequest firstRequest = requestManager.getRequest(0, VENDOR_ID);
        ServiceRequest secondRequest = requestManager.getRequest(1, VENDOR_ID);

        //Assert
        assertEquals("The request provided isn't the correct one", request1, firstRequest);
        assertEquals("The request provided isn't the correct one", request2, secondRequest);
    }


    @Test
    public void testGetAllRequests_success() {
        //Arrange
        ServiceRequest request1 = requestManager.addNewRequest(EVENT1, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);
        ServiceRequest request2 = requestManager.addNewRequest(EVENT2, VENDOR_ID_2, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET2);

        //Act
        List<ServiceRequest> retrievedRequests = requestManager.getAllRequests(VENDOR_ID);

        //Assert
        assertEquals("Expected to have 2 requests", 2, retrievedRequests.size());
        assertTrue("The request is not in the database", retrievedRequests.contains(request1));
        assertTrue("The request is not in the database", retrievedRequests.contains(request2));
    }

    @Test
    public void testGetAllRequestsOfEvent_success() {
        //Arrange
        ServiceRequest request1 = requestManager.addNewRequest(EVENT1, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);
        ServiceRequest request2 = requestManager.addNewRequest(EVENT2, VENDOR_ID_2, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET2);

        //Act
        List<ServiceRequest> retrievedRequests = requestManager.getAllRequestsOfEvent(EVENT1.getId());

        //Assert
        assertEquals("Expected to have 1 request", 1, retrievedRequests.size());
        assertTrue("The request is not in the database", retrievedRequests.contains(request1));
        assertFalse("The request should not be the database", retrievedRequests.contains(request2));
    }


    @Test
    public void testDeleteRequest_success() {
        //Arrange
        ServiceRequest request1 = requestManager.addNewRequest(EVENT1, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);

        //Act
        requestManager.deleteRequest(request1);

        //Assert
        List<ServiceRequest> finalRequests = requestManager.getAllRequests(VENDOR_ID);
        assertEquals("A request wasn't deleted successfully ", 0, finalRequests.size());
        assertFalse("A request wasn't deleted successfully ", finalRequests.contains(request1));
    }

    @Test
    public void testSetServiceStatus_success() {
        //Arrange
        ServiceRequest request1 = requestManager.addNewRequest(EVENT1, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);
        ServiceRequest.ServiceStatus intiStatus = request1.getServiceStatus();

        //Act
        requestManager.setServiceStatus(0, ServiceRequest.ServiceStatus.PENDING);

        //Assert
        ServiceRequest pendingRequest = requestManager.getRequest(0, VENDOR_ID);
        assertEquals("Request should have status NEW when created", ServiceRequest.ServiceStatus.NEW, intiStatus);
        assertEquals("Request should have status PENDING after setting", ServiceRequest.ServiceStatus.PENDING, pendingRequest.getServiceStatus());
    }
}
