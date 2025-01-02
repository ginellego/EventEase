package comp3350.a15.eventease.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class InvoiceTest {
    @Test
    public void testInvoice() {
        //Arrange
        final int requestId = 0;
        final int plannerId = 0;
        final String serviceType = "Catering";
        final String eventName = "New event";
        String eventDate = "2024-01-01";
        String eventTime = "12:00";
        final String eventLocation = "Event Location";
        final long offerAccepted = 200;

        //Act
        Invoice invoice = new Invoice(requestId, plannerId, serviceType, eventName, eventDate, eventTime, eventLocation, offerAccepted);

        // Assert
        assertNotNull("Invoice should not be null", invoice);
        assertEquals("Unexpected invoice requestId", requestId, invoice.getRequestId());
        assertEquals("Unexpected invoice plannerId", plannerId, invoice.getPlannerId());
        assertEquals("Unexpected invoice event name", eventName, invoice.getEventName());
        assertEquals("Unexpected invoice event location", eventLocation, invoice.getEventLocation());
        assertEquals("Unexpected invoice event date", eventDate, invoice.getEventDate());
        assertEquals("Unexpected invoice event time", eventTime, invoice.getEventTime());
        assertEquals("Unexpected invoice service type", serviceType, invoice.getServiceType());
        assertEquals("Unexpected invoice offer accepted", offerAccepted, invoice.getOfferAccepted());
    }
}
