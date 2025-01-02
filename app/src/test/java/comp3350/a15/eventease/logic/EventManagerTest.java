package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import comp3350.a15.eventease.logic.exceptions.EventAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.EventDateInThePastException;
import comp3350.a15.eventease.logic.exceptions.EventNotAddedException;
import comp3350.a15.eventease.logic.exceptions.EventNotDeletedException;
import comp3350.a15.eventease.logic.exceptions.EventNotFoundException;
import comp3350.a15.eventease.logic.exceptions.FieldMissingException;
import comp3350.a15.eventease.logic.implementation.EventManagerImpl;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.persistence_stub.EventPersistenceStub;

@RunWith(MockitoJUnitRunner.class)
public class EventManagerTest {
    private static final int PLANNER_ID = 1;
    private static final Timestamp TIME_STAMP_ONE = new Timestamp(System.currentTimeMillis());
    private static final Timestamp TIME_STAMP_TWO = new Timestamp(System.currentTimeMillis());
    private static final CharSequence EVENT1_NAME = "Test Event1";
    private static final String EVENT1_NAME_STRING = "Test Event1";
    private static final CharSequence EVENT2_NAME = "Test Event2";
    private static final String EVENT2_NAME_STRING = "Test Event2";
    private static final CharSequence EVENT_DESCRIPTION = "Test Description";
    private static final String EVENT_DESCRIPTION_STRING = "Test Description";
    private static final CharSequence EVENT_LOCATION = "Test Location";
    private static final String EVENT_LOCATION_STRING = "Test Location";
    private static final CharSequence EVENT_DATE = LocalDate.now().plusDays(1).toString();
    private static final LocalDate PARSED_DATE = LocalDate.parse(EVENT_DATE);
    private static final CharSequence EVENT_DATE_LATER = LocalDate.now().plusDays(2).toString();
    private static final LocalDate PARSED_DATE_LATER = LocalDate.parse(EVENT_DATE_LATER);
    private static final CharSequence EVENT_TIME = "00:00";
    private static final LocalTime PARSED_TIME = LocalTime.parse(EVENT_TIME);
    private static final CharSequence EVENT_NAME_VALID_POST_EDIT = "Edited Name";
    private static final String EVENT_NAME_VALID_POST_EDIT_STRING = "Edited Name";
    private static final CharSequence EVENT_DESCRIPTION_VALID_POST_EDIT = "Edited Description";
    private static final String EVENT_DESCRIPTION_VALID_POST_EDIT_STRING = "Edited Description";
    private static final CharSequence EVENT_LOCATION_VALID_POST_EDIT = "Edited Location";
    private static final String EVENT_LOCATION_VALID_POST_EDIT_STRING = "Edited Location";
    private static final CharSequence EVENT_DATE_VALID_POST_EDIT = LocalDate.now().plusDays(2).toString();
    private static final LocalDate PARSED_EVENT_DATE_VALID_POST_EDIT = LocalDate.parse(EVENT_DATE_VALID_POST_EDIT);
    private static final CharSequence EVENT_TIME_VALID_POST_EDIT = "07:20";
    private static final LocalTime PARSED_EVENT_TIME_VALID_POST_EDIT = LocalTime.parse(EVENT_TIME_VALID_POST_EDIT);
    private static IEventManager eventManager;
    @Mock
    private
    IEventFactory eventFactory;

    @Before
    public void testSetup() {
        eventManager = new EventManagerImpl(new EventPersistenceStub(), eventFactory);
        eventManager.setSortParameter("");

        when(eventFactory.create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(PARSED_DATE), eq(PARSED_TIME), any())).thenReturn(
                new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                        PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE));

        when(eventFactory.create(eq(PLANNER_ID), eq(EVENT2_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(PARSED_DATE), eq(PARSED_TIME), any())).thenReturn(
                new Event(PLANNER_ID, EVENT2_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                        PARSED_DATE, PARSED_TIME, TIME_STAMP_TWO));

    }

    @Test
    public void testAddNewEvent_success() {
        // Act
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        // Assert
        verify(eventFactory).create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(PARSED_DATE), eq(PARSED_TIME), any());
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertNotNull("Event should not be null", event);
        assertEquals("Unexpected number of events", 1, finalEvents.size());
        assertTrue("Event should be in the list", finalEvents.contains(event));
    }

    @Test
    public void testAddNewEvent_pastDate_throwsEventDateInThePastException() {
        // Arrange
        LocalDate previousDay = LocalDate.now().minusDays(1);
        when(eventFactory.create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(previousDay), eq(PARSED_TIME), any())).thenReturn(
                new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                        previousDay, PARSED_TIME, TIME_STAMP_ONE));

        //Act
        assertThrows("Event date cannot be in the past", EventDateInThePastException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                        previousDay.toString(), EVENT_TIME));

        verify(eventFactory).create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(previousDay), eq(PARSED_TIME), any());
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Unexpected number of events", 0, finalEvents.size());
    }

    @Test
    public void testAddNewEvent_duplicateEvent_throwsEventAlreadyExistsException() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        assertThrows("Event already exists", EventAlreadyExistsException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                        EVENT_DATE, EVENT_TIME));

        verify(eventFactory, times(2)).create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(PARSED_DATE), eq(PARSED_TIME), any());
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Unexpected number of events", 1, finalEvents.size());
        assertTrue("Event should be in the list", finalEvents.contains(event));
    }

    @Test
    public void testAddNewEvent_fieldEmpty_throwsFieldMissingException() {
        // Arrange
        Event event = new Event(PLANNER_ID, "", EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE);

        //Act
        assertThrows("Fields missing", FieldMissingException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, "", EVENT_DESCRIPTION, EVENT_LOCATION,
                        EVENT_DATE, EVENT_TIME));

        verify(eventFactory, never()).create(anyInt(), anyString(), anyString(), anyString(), any(), any(), any());
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Unexpected number of events", 0, finalEvents.size());
        assertFalse("Event should not be in the list", finalEvents.contains(event));
    }

    @Test
    public void testAddNewEvent_fieldNull_throwsFieldMissingException() {
        // Arrange
        Event event = new Event(PLANNER_ID, null, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE);

        //Act
        assertThrows("Fields missing", FieldMissingException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, null, EVENT_DESCRIPTION, EVENT_LOCATION,
                        EVENT_DATE, EVENT_TIME));

        verify(eventFactory, never()).create(anyInt(), anyString(), anyString(), anyString(), any(), any(), any());
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Unexpected number of events", 0, finalEvents.size());
        assertFalse("Event should not be in the list", finalEvents.contains(event));
    }

    @Test
    public void testAddNewEvent_throwsEventNotAddedException() {
        // Arrange
        Event event = new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                EVENT_LOCATION_STRING, PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE);
        EventPersistenceStub mockedEventPersistence = mock(EventPersistenceStub.class);
        eventManager = new EventManagerImpl(mockedEventPersistence, eventFactory);
        when(mockedEventPersistence.addNewEvent(event)).thenReturn(false);

        //Act
        assertThrows("Error adding event to database", EventNotAddedException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION,
                        EVENT_LOCATION, EVENT_DATE, EVENT_TIME));

        // Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Unexpected number of events", 0, finalEvents.size());
        assertFalse("Event should not be in the list", finalEvents.contains(event));
    }

    @Test
    public void testGetEvent_success() {
        //Arrange
        Event event1 = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        Event event2 = eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        Event firstEvent = eventManager.getEvent(0, PLANNER_ID);
        Event secondEvent = eventManager.getEvent(1, PLANNER_ID);

        //Assert
        assertEquals("First event retrieved should be equal to event1", event1, firstEvent);
        assertEquals("Second event retrieved should be equal to event2", event2, secondEvent);
    }

    @Test
    public void testGetEvent_throwsEventNotFoundException() {
        //Arrange

        //Act and Assert
        assertThrows("Event could not be found", EventNotFoundException.class,
                () -> eventManager.getEvent(2, PLANNER_ID));
    }

    @Test
    public void testGetAllEvents_success() {
        //Arrange
        Event event1 = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        Event event2 = eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        List<Event> retrievedEvents = eventManager.getAllEvents(PLANNER_ID);

        //Assert
        assertEquals("Number of retrieved events should be 2", 2, retrievedEvents.size());
        assertTrue("Retrieved events should contain event1", retrievedEvents.contains(event1));
        assertTrue("Retrieved events should contain event2", retrievedEvents.contains(event2));
    }

    @Test
    public void testEditEventName_success() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        when(eventFactory.create(anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn(
                new Event(PLANNER_ID, EVENT_NAME_VALID_POST_EDIT_STRING, EVENT_DESCRIPTION_STRING,
                        EVENT_LOCATION_STRING, PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE));

        //Act
        eventManager.editEvent(event, EVENT_NAME_VALID_POST_EDIT, EVENT_DESCRIPTION_STRING,
                EVENT_LOCATION_STRING, EVENT_DATE, EVENT_TIME);

        //Assert
        verify(eventFactory).create(eq(PLANNER_ID), eq(EVENT_NAME_VALID_POST_EDIT_STRING), eq(EVENT_DESCRIPTION_STRING),
                eq(EVENT_LOCATION_STRING), eq(PARSED_DATE), eq(PARSED_TIME), any());
        assertEquals("Event name should be updated after edit", EVENT_NAME_VALID_POST_EDIT, eventManager.getEvent(0, PLANNER_ID).getEventName());
    }

    @Test
    public void testEditEventName_throwsEventNotEditedException() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        assertThrows("Expected FieldMissingException when event name is empty", FieldMissingException.class,
                () -> eventManager.editEvent(event, "", EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME)
        );
    }

    @Test
    public void testEditEventLocation_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        when(eventFactory.create(anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn(
                new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                        EVENT_LOCATION_VALID_POST_EDIT_STRING, PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE));

        // Act
        eventManager.editEvent(event, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION_VALID_POST_EDIT,
                EVENT_DATE, EVENT_TIME);

        // Assert
        verify(eventFactory).create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_VALID_POST_EDIT_STRING),
                eq(PARSED_DATE), eq(PARSED_TIME), any());
        assertEquals("Event location should be updated after edit", EVENT_LOCATION_VALID_POST_EDIT, eventManager.getEvent(0, PLANNER_ID).getEventLocation());
    }

    @Test
    public void testEditEventLocation_throwsEventNotEditedException() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        assertThrows("Field missing", FieldMissingException.class,
                () -> eventManager.editEvent(event, EVENT1_NAME, EVENT_DESCRIPTION, "",
                        EVENT_DATE, EVENT_TIME)
        );
    }

    @Test
    public void testEditEventDescription_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        when(eventFactory.create(anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn(
                new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_VALID_POST_EDIT_STRING,
                        EVENT_LOCATION_STRING, PARSED_DATE, PARSED_TIME, TIME_STAMP_ONE));

        // Act
        eventManager.editEvent(event, EVENT1_NAME_STRING, EVENT_DESCRIPTION_VALID_POST_EDIT,
                EVENT_LOCATION_STRING, EVENT_DATE, EVENT_TIME);

        // Assert
        verify(eventFactory).create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_VALID_POST_EDIT_STRING),
                eq(EVENT_LOCATION_STRING), eq(PARSED_DATE), eq(PARSED_TIME), any());
        assertEquals("Event description should be updated after edit", EVENT_DESCRIPTION_VALID_POST_EDIT, eventManager.getEvent(0, PLANNER_ID).getEventDescription());
    }

    @Test
    public void testEditEventDescription_throwsEventNotEditedException() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        assertThrows("Field missing", FieldMissingException.class,
                () -> eventManager.editEvent(event, EVENT1_NAME, "",
                        EVENT_LOCATION, EVENT_DATE, EVENT_TIME)
        );
    }

    @Test
    public void testEditEventDate_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        when(eventFactory.create(anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn(
                new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                        EVENT_LOCATION_STRING, PARSED_EVENT_DATE_VALID_POST_EDIT, PARSED_TIME, TIME_STAMP_ONE));

        // Act
        eventManager.editEvent(event, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                EVENT_LOCATION_STRING, EVENT_DATE_VALID_POST_EDIT, EVENT_TIME);

        // Assert
        verify(eventFactory).create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(PARSED_EVENT_DATE_VALID_POST_EDIT), eq(PARSED_TIME), any());
        assertEquals("Event date should be updated after edit", PARSED_EVENT_DATE_VALID_POST_EDIT, eventManager.getEvent(0, PLANNER_ID).getEventDate());
    }

    @Test
    public void testEditEventTime_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        when(eventFactory.create(anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn(
                new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                        EVENT_LOCATION_STRING, PARSED_DATE, PARSED_EVENT_TIME_VALID_POST_EDIT, TIME_STAMP_ONE));

        // Act
        eventManager.editEvent(event, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                EVENT_LOCATION_STRING, EVENT_DATE, EVENT_TIME_VALID_POST_EDIT);

        // Assert
        verify(eventFactory).create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(PARSED_DATE), eq(PARSED_EVENT_TIME_VALID_POST_EDIT), any());
        assertEquals("Event time should be updated after edit", PARSED_EVENT_TIME_VALID_POST_EDIT, eventManager.getEvent(0, PLANNER_ID).getEventTime());
    }

    @Test
    public void testDeleteEvent_success() {
        //Arrange
        Event event1 = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        eventManager.deleteEvent(event1);

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Number of events should be 0 after deletion", 0, finalEvents.size());
        assertFalse("Deleted event should not be in the list of events", finalEvents.contains(event1));
    }

    @Test
    public void testDeleteEvent_errorDeletingEvent_throwsEventNotDeletedException() {
        //Arrange
        EventPersistenceStub mockedEventPersistence = mock(EventPersistenceStub.class);
        eventManager = new EventManagerImpl(mockedEventPersistence, eventFactory);
        when(mockedEventPersistence.addNewEvent(any())).thenReturn(true);
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        when(mockedEventPersistence.deleteEvent(event)).thenReturn(false);


        //Act
        assertThrows("Event could not be deleted",
                EventNotDeletedException.class, () -> eventManager.deleteEvent(event));

        //Assert
        verify(mockedEventPersistence).addNewEvent(event);
        verify(mockedEventPersistence).doesEventExist(event);
        verify(mockedEventPersistence).deleteEvent(event);
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Number of events should be 0 after deletion", 0, finalEvents.size());
        assertFalse("Deleted event should not be in the list of events", finalEvents.contains(event));
    }

    @Test
    public void testSetSortParameter_and_getAllEvents_sortedByName() {
        // Arrange
        eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);

        // Act
        eventManager.setSortParameter("Name");
        List<Event> sortedEvents = eventManager.getAllEvents(PLANNER_ID);

        // Assert
        assertEquals("First event should be sorted by name", EVENT1_NAME_STRING, sortedEvents.get(0).getEventName());
        assertEquals("Second event should be sorted by name", EVENT2_NAME_STRING, sortedEvents.get(1).getEventName());
    }

    @Test
    public void testSetSortParameter_and_getAllEvents_sortedByEventDate() {
        //Arrange
        when(eventFactory.create(eq(PLANNER_ID), eq(EVENT1_NAME_STRING), eq(EVENT_DESCRIPTION_STRING), eq(EVENT_LOCATION_STRING),
                eq(PARSED_DATE_LATER), eq(PARSED_TIME), any())).thenReturn(
                new Event(PLANNER_ID, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING, EVENT_LOCATION_STRING,
                        PARSED_DATE_LATER, PARSED_TIME, TIME_STAMP_ONE));
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE_LATER, EVENT_TIME);
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);

        // Act
        eventManager.setSortParameter("Event Date");
        List<Event> sortedEvents = eventManager.getAllEvents(PLANNER_ID);

        // Assert
        assertEquals("First event should be sorted by event date", PARSED_DATE, sortedEvents.get(0).getEventDate());
        assertEquals("Second event should be sorted by event date", PARSED_DATE_LATER, sortedEvents.get(1).getEventDate());
    }


    @Test
    public void testSetSortParameter_and_getAllEvents_sortedByDateAdded_oldToNew() {
        //Arrange
        eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);

        // Act
        eventManager.setSortParameter("Date Added(old -> new)");
        List<Event> sortedEvents = eventManager.getAllEvents(PLANNER_ID);

        // Assert
        assertEquals("First event should be sorted by date added (old -> new)", TIME_STAMP_ONE, sortedEvents.get(0).getWhenCreated());
        assertEquals("Second event should be sorted by date added (old -> new)", TIME_STAMP_TWO, sortedEvents.get(1).getWhenCreated());
    }

    @Test
    public void testSetSortParameter_and_getAllEvents_sortedByDateAdded_newToOld() {
        //Arrange
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);
        eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);

        // Act
        eventManager.setSortParameter("Date Added(new -> old)");
        List<Event> sortedEvents = eventManager.getAllEvents(PLANNER_ID);

        // Assert
        assertEquals("First event should be sorted by date added (new -> old)", TIME_STAMP_TWO, sortedEvents.get(0).getWhenCreated());
        assertEquals("Second event should be sorted by date added (new -> old)", TIME_STAMP_ONE, sortedEvents.get(1).getWhenCreated());
    }
}
