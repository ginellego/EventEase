package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;

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

import comp3350.a15.eventease.TestUtils;
import comp3350.a15.eventease.logic.implementation.RequestInvoiceManagerImpl;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.objects.factory.IInvoiceFactory;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.objects.factory.implementation.EventFactoryImpl;
import comp3350.a15.eventease.objects.factory.implementation.RequestInvoiceFactoryImpl;
import comp3350.a15.eventease.objects.factory.implementation.ServiceRequestFactoryImpl;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;
import comp3350.a15.eventease.persistence.hsqldb.AcceptedServiceRequestsHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.EventPersistenceImplHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.InvoicePersistenceImplHSQLDB;

@RunWith(JUnit4.class)
public class RequestInvoiceManagerIT {
    private static final int PLANNER_ID = 0;
    private static final Timestamp TIME_STAMP = new Timestamp(System.currentTimeMillis());
    private static final String EVENT1_NAME_STRING = "Test Event1";
    private static final String EVENT_DESCRIPTION_STRING = "Test Description";
    private static final String EVENT_LOCATION_STRING = "Test Location";
    private static final CharSequence EVENT_DATE = LocalDate.now().plusDays(1).toString();
    private static final LocalDate PARSED_DATE = LocalDate.parse(EVENT_DATE);
    private static final LocalDate PARSED_DATE_SERVICE = LocalDate.parse(EVENT_DATE);
    private static final CharSequence EVENT_TIME = "00:00";
    private static final LocalTime PARSED_TIME = LocalTime.parse(EVENT_TIME);
    private static final int VENDOR_ID = 1;
    private static final String SERVICE_TYPE_STRING = "CAKE";
    private static final long BUDGET = 1000;
    private static final long BUDGET2 = 15000;
    private static final ServiceRequest.ServiceStatus SERVICE_STATUS = ServiceRequest.ServiceStatus.NEW;
    private final int requestId = 1;
    private final String serviceType = "Music";
    private final String eventName = "party";
    private final String eventDate = "25-04-2024";
    private final String eventTime = "12:05";
    private final String eventLocation = "somewhere far";
    private final long offerAccepted = (long) 10.5;
    private final int id = 1;
    private RequestInvoiceManagerImpl requestInvoiceManager;
    private InvoicePersistenceImplHSQLDB invoicePersistence;
    private ServiceRequest serviceRequest;
    private File tempDB;

    @Before
    public void testSetup() throws IOException {
        tempDB = TestUtils.copyDB();
        IInvoiceFactory invoiceFactory = new RequestInvoiceFactoryImpl();
        IEventFactory eventFactory = new EventFactoryImpl();
        IServiceRequestFactory serviceRequestFactory = new ServiceRequestFactoryImpl();
        IEventPersistence eventPersistence = new EventPersistenceImplHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), eventFactory);
        invoicePersistence = new InvoicePersistenceImplHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), invoiceFactory);
        requestInvoiceManager = new RequestInvoiceManagerImpl(invoicePersistence, invoiceFactory);
        Event event = new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                PARSED_DATE, PARSED_TIME, TIME_STAMP);


        eventPersistence.addNewEvent(event);


        int plannerId = 0;
        event = eventPersistence.getEvent(event.getId(), plannerId);

        serviceRequest = new ServiceRequest(event, VENDOR_ID, SERVICE_TYPE_STRING, PARSED_DATE_SERVICE, BUDGET,
                SERVICE_STATUS);

        IServiceRequestPersistence serviceRequestPersistence = new AcceptedServiceRequestsHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), serviceRequestFactory, eventPersistence);
        serviceRequestPersistence.addNewRequest(serviceRequest);
    }

    @Test
    public void testGenerateInvoice() {
        // Arrange
        int invoiceId = requestInvoiceManager.generateInvoice(serviceRequest);

        // Act
        Invoice generatedInvoice = invoicePersistence.getInvoice(invoiceId);

        // Assert
        assertEquals(invoiceId, generatedInvoice.getId());
    }

    @Test
    public void testGetInvoice() {
        // Arrange
        int invoiceId = requestInvoiceManager.generateInvoice(serviceRequest);

        // Act
        Invoice retrievedInvoice = requestInvoiceManager.getInvoice(invoiceId);

        // Assert
        assertEquals(invoiceId, retrievedInvoice.getId());
    }

    @Test
    public void testGetInvoiceByRequestId() {
        // Arrange
        int invoiceId = requestInvoiceManager.generateInvoice(serviceRequest);

        // Act
        Invoice retrievedInvoice = requestInvoiceManager.getInvoiceByRequestId(serviceRequest.getId());

        // Assert
        assertEquals(invoiceId, retrievedInvoice.getId());
    }

    @After
    public void tearDown() {
        // reset DB
        tempDB.delete();

    }
}
