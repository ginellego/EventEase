package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import comp3350.a15.eventease.TestUtils;
import comp3350.a15.eventease.logic.implementation.UnresolvedServiceRequestManager;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.objects.factory.implementation.EventFactoryImpl;
import comp3350.a15.eventease.objects.factory.implementation.ServiceRequestFactoryImpl;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;
import comp3350.a15.eventease.persistence.hsqldb.AcceptedServiceRequestsHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.EventPersistenceImplHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.RejectedServiceRequestsHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.UnresolvedServiceRequestsHSQLDB;

@RunWith(JUnit4.class)
public class BaseServiceRequestManagerIT {
    private static final int PLANNER_ID = 0;
    private static final Timestamp TIME_STAMP = new Timestamp(System.currentTimeMillis());
    private static final String EVENT1_NAME_STRING = "Test Event1";
    private static final String EVENT_DESCRIPTION_STRING = "Test Description";
    private static final String EVENT_LOCATION_STRING = "Test Location";
    private static final CharSequence EVENT_DATE = LocalDate.now().plusDays(1).toString();
    private static final LocalDate PARSED_DATE = LocalDate.parse(EVENT_DATE);
    private static final LocalDate PARSED_DATE_SERVICE = LocalDate.parse(EVENT_DATE);
    private static final LocalDate PARSED_LATER_DATE_SERVICE = PARSED_DATE_SERVICE.plusDays(2);
    private static final CharSequence EVENT_TIME = "00:00";
    private static final LocalTime PARSED_TIME = LocalTime.parse(EVENT_TIME);
    private static final int VENDOR_ID = 1;
    private static final String SERVICE_TYPE_STRING = "CAKE";
    private static final long BUDGET = 1000;
    private static final long BUDGET2 = 15000;
    private static final ServiceRequest.ServiceStatus SERVICE_STATUS = ServiceRequest.ServiceStatus.NEW;
    private static IServiceRequestManager requestManager;
    private Event exisitingEvent;
    private File tempDB;

    @Before
    public void testSetup() throws IOException {
        tempDB = TestUtils.copyDB();

        IServiceRequestFactory serviceRequestFactory = new ServiceRequestFactoryImpl();
        IEventFactory eventFactory = new EventFactoryImpl();

        IEventPersistence eventPersistence = new EventPersistenceImplHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), eventFactory);

        exisitingEvent = new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                PARSED_DATE, PARSED_TIME, TIME_STAMP);
        eventPersistence.addNewEvent(exisitingEvent);
        IServiceRequestPersistence persistence = getiServiceRequestPersistence(serviceRequestFactory, eventPersistence);
        requestManager = new UnresolvedServiceRequestManager(persistence, serviceRequestFactory);


    }

    @NonNull
    private IServiceRequestPersistence getiServiceRequestPersistence(IServiceRequestFactory serviceRequestFactory, IEventPersistence eventPersistence) {
        IServiceRequestPersistence persistence = new AcceptedServiceRequestsHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), serviceRequestFactory, eventPersistence);
        persistence.getAllRequests(VENDOR_ID);
        persistence = new RejectedServiceRequestsHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), serviceRequestFactory, eventPersistence);
        persistence.getAllRequests(VENDOR_ID);
        persistence = new UnresolvedServiceRequestsHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), serviceRequestFactory, eventPersistence);
        return persistence;
    }

    @Test
    public void testAddServiceRequestIT_success() {
        //Arrange
        ServiceRequest request = new ServiceRequest(exisitingEvent, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET,
                SERVICE_STATUS);

        //Act
        ServiceRequest request2 = requestManager.addRequest(request);

        //Assert
        List<ServiceRequest> finalEvents = requestManager.getAllRequests(VENDOR_ID);
        assertEquals("The request was not added successfully", 6, finalEvents.size());
        assertTrue(finalEvents.contains(request2));


    }


    @Test
    public void testAddNewServiceNewRequestIT_success() {

        //Act
        ServiceRequest request = requestManager.addNewRequest(exisitingEvent, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET2);


        //Assert
        List<ServiceRequest> finalEvents = requestManager.getAllRequests(VENDOR_ID);
        assertEquals("New request was not added successfully", 6, finalEvents.size());
        assertTrue("The newly added request is not in the final list", finalEvents.contains(request));
    }

    @Test
    public void testGetRequestIT_success() {
        // Arrange
        ServiceRequest request1 = requestManager.addNewRequest(exisitingEvent, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);
        ServiceRequest request2 = requestManager.addNewRequest(exisitingEvent, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_LATER_DATE_SERVICE, BUDGET2);

        // Act
        ServiceRequest firstRequest = requestManager.getRequest(request1.getId(), VENDOR_ID);
        ServiceRequest secondRequest = requestManager.getRequest(request2.getId(), VENDOR_ID);

        // Assert
        assertEquals("The request provided isn't the correct one", request1, firstRequest);
        assertEquals("The request provided isn't the correct one", request2, secondRequest);
    }

    @Test
    public void testGetAllRequestsIT_success() {
        // Arrange

        // Act
        List<ServiceRequest> retrievedRequests = requestManager.getAllRequests(VENDOR_ID);

        // Assert
        assertEquals("Not all requests are being shown", 5, retrievedRequests.size());
    }


    @Test
    public void testDeleteRequestIT_success() {
        // Arrange
        ServiceRequest request1 = requestManager.addNewRequest(exisitingEvent, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);

        // Act
        requestManager.deleteRequest(request1);

        // Assert
        // ensure the request is no longer present in the retrieved list
        List<ServiceRequest> finalRequests = requestManager.getAllRequests(VENDOR_ID);
        assertEquals("The request wasn't deleted successfully ", 5, finalRequests.size());
        assertFalse("The request wasn't deleted successfully ", finalRequests.contains(request1));
    }

    @Test
    public void testSetStatusIT_success() {
        // Arrange
        ServiceRequest request1 = requestManager.addNewRequest(exisitingEvent, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET);

        // Act
        requestManager.setServiceStatus(request1.getId(), ServiceRequest.ServiceStatus.PENDING);

        // Assert
        ServiceRequest retrievedRequest = requestManager.getRequest(request1.getId(), VENDOR_ID);
        assertNotEquals("The request should no longer be NEW", request1.getServiceStatus(), retrievedRequest.getServiceStatus());
        assertEquals("The status of the request should be PENDING", ServiceRequest.ServiceStatus.PENDING, retrievedRequest.getServiceStatus());
    }

    @After
    public void tearDown() {
        // reset DB
        tempDB.delete();

    }


}
