package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;

import comp3350.a15.eventease.logic.implementation.RequestInvoiceManagerImpl;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IInvoiceFactory;
import comp3350.a15.eventease.persistence.IRequestInvoicePersistence;

@RunWith(MockitoJUnitRunner.class)
public class RequestInvoiceManagerTest {
    private RequestInvoiceManagerImpl requestInvoiceManager;
    private IRequestInvoicePersistence mockInvoicePersistence;
    private IInvoiceFactory mockInvoiceFactory;
    private ServiceRequest mockServiceRequest;

    @Before
    public void setUp() {
        mockInvoicePersistence = mock(IRequestInvoicePersistence.class);
        mockInvoiceFactory = mock(IInvoiceFactory.class);
        requestInvoiceManager = new RequestInvoiceManagerImpl(mockInvoicePersistence, mockInvoiceFactory);

        mockServiceRequest = mock(ServiceRequest.class);
        Event mockEvent = mock(Event.class);
        when(mockServiceRequest.getId()).thenReturn(1);
        when(mockServiceRequest.getAssociatedEvent()).thenReturn(mockEvent);
        when(mockEvent.getPlannerId()).thenReturn(1);
        when(mockEvent.getEventName()).thenReturn("EventName");
        when(mockEvent.getEventDate()).thenReturn(LocalDate.now());
        when(mockEvent.getEventTime()).thenReturn(LocalTime.now());
        when(mockEvent.getEventLocation()).thenReturn("EventLocation");
        when(mockServiceRequest.getServiceType()).thenReturn("ServiceType");
        when(mockServiceRequest.getBudget()).thenReturn((long) 1000.0);
    }

    @Test
    public void testGenerateInvoice() {
        //Arrange
        Invoice mockInvoice = mock(Invoice.class);
        when(mockInvoice.getId()).thenReturn(123);

        // Act
        when(mockInvoiceFactory.create(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(mockInvoice);


        int invoiceId = requestInvoiceManager.generateInvoice(mockServiceRequest);

        // Assert
        verify(mockInvoicePersistence).addInvoice(mockInvoice);
        assertEquals(123, invoiceId);
    }

    @Test
    public void testGetInvoice() {
        //Arrange
        Invoice mockInvoice = mock(Invoice.class);
        when(mockInvoicePersistence.getInvoice(1)).thenReturn(mockInvoice);

        //Act
        Invoice retrievedInvoice = requestInvoiceManager.getInvoice(1);

        // Assert
        assertEquals(mockInvoice, retrievedInvoice);
    }

    @Test
    public void testGetInvoiceByRequestId() {
        // Arrange
        Invoice mockInvoice = mock(Invoice.class);
        when(mockInvoicePersistence.getInvoiceByRequestId(1)).thenReturn(mockInvoice);

        // Act
        Invoice retrievedInvoice = requestInvoiceManager.getInvoiceByRequestId(1);

        // Assert
        assertEquals(mockInvoice, retrievedInvoice);
    }


}
