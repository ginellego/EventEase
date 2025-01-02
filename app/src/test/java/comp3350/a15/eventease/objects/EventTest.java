package comp3350.a15.eventease.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventTest {
    @Test
    public void testEvent() {
        //Arrange
        final String eventName = "Event Name";
        final String eventDescription = "Event Description";
        final String eventLocation = "Event Location";
        LocalDate eventDate = LocalDate.parse("2024-01-01");
        LocalTime eventTime = LocalTime.parse("12:00");
        final int plannerId = 1;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Act
        Event event = new Event(plannerId, eventName, eventDescription, eventLocation, eventDate, eventTime, timestamp);

        // Assert
        assertNotNull("Event should not be null", event);
        assertEquals("Unexpected event name", eventName, event.getEventName());
        assertEquals("Unexpected event description", eventDescription, event.getEventDescription());
        assertEquals("Unexpected event location", eventLocation, event.getEventLocation());
        assertEquals("Unexpected event date", eventDate, event.getEventDate());
        assertEquals("Unexpected event time", eventTime, event.getEventTime());
    }
}
