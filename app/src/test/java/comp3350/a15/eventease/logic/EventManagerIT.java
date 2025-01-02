package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import comp3350.a15.eventease.TestUtils;
import comp3350.a15.eventease.logic.exceptions.EventAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.EventDateInThePastException;
import comp3350.a15.eventease.logic.exceptions.EventNotDeletedException;
import comp3350.a15.eventease.logic.exceptions.EventNotFoundException;
import comp3350.a15.eventease.logic.exceptions.FieldMissingException;
import comp3350.a15.eventease.logic.implementation.EventManagerImpl;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.objects.factory.implementation.EventFactoryImpl;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.hsqldb.EventPersistenceImplHSQLDB;

@RunWith(JUnit4.class)
public class EventManagerIT {
    private static final int PLANNER_ID = 0;
    private static final CharSequence EVENT1_NAME = "An Event1";
    private static final String EVENT1_NAME_STRING = "An Event1";
    private static final CharSequence EVENT2_NAME = "An Event2";
    private static final String EVENT2_NAME_STRING = "An Event2";
    private static final CharSequence EVENT_DESCRIPTION = "Test Description";
    private static final String EVENT_DESCRIPTION_STRING = "Test Description";
    private static final CharSequence EVENT_LOCATION = "Test Location";
    private static final String EVENT_LOCATION_STRING = "Test Location";
    private static final CharSequence EVENT_DATE = LocalDate.now().plusDays(1).toString();
    private static final LocalDate PARSED_DATE = LocalDate.parse(EVENT_DATE);
    private static final CharSequence EVENT_DATE_LATER = LocalDate.now().plusDays(2).toString();
    private static final LocalDate PARSED_DATE_LATER = LocalDate.parse(EVENT_DATE_LATER);
    private static final CharSequence EVENT_TIME = "00:00";
    private static final CharSequence EVENT_NAME_VALID_POST_EDIT = "Edited Name";
    private static final CharSequence EVENT_LOCATION_VALID_POST_EDIT = "Edited Location";
    private static final CharSequence EVENT_DESCRIPTION_VALID_POST_EDIT = "Edited Description";
    private static final CharSequence EVENT_DATE_VALID_POST_EDIT = LocalDate.now().plusDays(2).toString();
    private static final LocalDate PARSED_EVENT_DATE_VALID_POST_EDIT = LocalDate.parse(EVENT_DATE_VALID_POST_EDIT);
    private static final CharSequence EVENT_TIME_VALID_POST_EDIT = "07:20";
    private static final LocalTime PARSED_EVENT_TIME_VALID_POST_EDIT = LocalTime.parse(EVENT_TIME_VALID_POST_EDIT);
    private static IEventManager eventManager;
    private File tempDB;

    @Before
    public void testSetup() throws IOException {
        tempDB = TestUtils.copyDB();
        IEventFactory eventFactory = new EventFactoryImpl();
        IEventPersistence persistence = new EventPersistenceImplHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), eventFactory);
        eventManager = new EventManagerImpl(persistence, eventFactory);
    }

    @Test
    public void testAddNewEventIT_success() {
        //Act
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("There should be 6 events, since 5 were initially present", 6, finalEvents.size());
        assertTrue(finalEvents.contains(event));
    }

    @Test
    public void testAddNewEventIT_pastDate_throwsEventDateInThePastException() {
        //Arrange
        LocalDate previousDay = LocalDate.now().minusDays(1);

        //Act
        assertThrows("Event date cannot be in the past", EventDateInThePastException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                        previousDay.toString(), EVENT_TIME));

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("There should be 5 which were initially present", 5, finalEvents.size());
    }

    @Test
    public void testAddNewEventIT_duplicateEvent_throwsEventAlreadyExistsException() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        assertThrows("Event already exists", EventAlreadyExistsException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                        EVENT_DATE, EVENT_TIME));

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("There should be 5 which were initially present", 6, finalEvents.size());
        assertTrue(finalEvents.contains(event));
    }

    @Test
    public void testAddNewEventIT_fieldEmpty_throwsFieldMissingException() {
        //Arrange

        //Act
        assertThrows("Fields missing", FieldMissingException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, "", EVENT_DESCRIPTION, EVENT_LOCATION,
                        EVENT_DATE, EVENT_TIME));

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("There should be 5 which were initially present", 5, finalEvents.size());
    }

    @Test
    public void testAddNewEventIT_fieldNull_throwsFieldMissingException() {
        //Arrange

        //Act
        assertThrows("Fields missing", FieldMissingException.class,
                () -> eventManager.addNewEvent(PLANNER_ID, null, EVENT_DESCRIPTION, EVENT_LOCATION,
                        EVENT_DATE, EVENT_TIME));

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("There should be 5 which were initially present", 5, finalEvents.size());
    }


    @Test
    public void testGetEventIT_success() {
        //Arrange
        Event event1 = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        Event event2 = eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        Event firstEvent = eventManager.getEvent(event1.getId(), PLANNER_ID);
        Event secondEvent = eventManager.getEvent(event2.getId(), PLANNER_ID);

        //Assert
        assertEquals("The returned event does not match the expected", event1, firstEvent);
        assertEquals("The returned event does not match the expected", secondEvent, event2);
    }

    @Test
    public void testGetEventIT_throwsEventNotAddedException() {
        //Arrange

        //Act and Assert
        assertThrows("Event could not be found", EventNotFoundException.class,
                () -> eventManager.getEvent(/*random integer(incorrect eventId)*/-1, PLANNER_ID));
    }

    @Test
    public void testGetAllEventsIT_success() {
        //Arrange
        Event event1 = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);
        Event event2 = eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        List<Event> retrievedEvents = eventManager.getAllEvents(PLANNER_ID);

        //Assert
        assertEquals("The expected count of existing events should be 7", 7, retrievedEvents.size());
        assertTrue("event1 is not contained in the DB", retrievedEvents.contains(event1));
        assertTrue("event2 is not contained in the DB", retrievedEvents.contains(event2));
    }

    @Test
    public void testEditEventNameIT_success() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        Event editedEvent = eventManager.editEvent(event, EVENT_NAME_VALID_POST_EDIT, EVENT_DESCRIPTION_STRING,
                EVENT_LOCATION_STRING, EVENT_DATE, EVENT_TIME);

        //Assert
        assertEquals("The name was not edited successfully", EVENT_NAME_VALID_POST_EDIT, editedEvent.getEventName());
    }

    @Test
    public void testEditEventNameIT_throwsFieldsMissingException() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        assertThrows("Field missing", FieldMissingException.class,
                () -> eventManager.editEvent(event, "", EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME)
        );
    }

    @Test
    public void testEditEventLocationIT_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        // Act
        Event editedEvent = eventManager.editEvent(event, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION_VALID_POST_EDIT,
                EVENT_DATE, EVENT_TIME);

        // Assert
        assertEquals("The event location was not edited successfully", EVENT_LOCATION_VALID_POST_EDIT, editedEvent.getEventLocation());
    }

    @Test
    public void testEditEventLocationIT_throwsFieldMissingException() {
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
    public void testEditEventDescriptionIT_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        // Act
        Event editedEvent = eventManager.editEvent(event, EVENT1_NAME_STRING, EVENT_DESCRIPTION_VALID_POST_EDIT,
                EVENT_LOCATION_STRING, EVENT_DATE, EVENT_TIME);

        // Assert
        assertEquals("The description was not edited successfully", EVENT_DESCRIPTION_VALID_POST_EDIT, editedEvent.getEventDescription());
    }

    @Test
    public void testEditEventDescriptionIT_throwsFieldMissingException() {
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
    public void testEditEventDateIT_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);


        // Act
        Event editedEvent = eventManager.editEvent(event, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                EVENT_LOCATION_STRING, EVENT_DATE_VALID_POST_EDIT, EVENT_TIME);

        // Assert

        assertEquals("The date of the event was not deleted successfully", PARSED_EVENT_DATE_VALID_POST_EDIT, editedEvent.getEventDate());
    }

    @Test
    public void testEditEventTimeIT_success() {
        // Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);


        // Act
        Event editedEvent = eventManager.editEvent(event, EVENT1_NAME_STRING, EVENT_DESCRIPTION_STRING,
                EVENT_LOCATION_STRING, EVENT_DATE, EVENT_TIME_VALID_POST_EDIT);

        // Assert
        assertEquals("The time of the event was not edited successfully", PARSED_EVENT_TIME_VALID_POST_EDIT, editedEvent.getEventTime());
    }

    @Test
    public void testDeleteEventIT_success() {
        //Arrange
        Event event1 = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        eventManager.deleteEvent(event1);

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Expected count of existing events after delete should be 5", 5, finalEvents.size());
        assertFalse("The existing events should not include event1", finalEvents.contains(event1));
    }

    @Test
    public void testDeleteEventIT_errorDeletingEvent_throwsEventNotDeletedException() {
        //Arrange
        Event event = eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION,
                EVENT_DATE, EVENT_TIME);

        //Act
        eventManager.deleteEvent(event);
        assertThrows("Event could not be deleted",
                EventNotDeletedException.class, () -> eventManager.deleteEvent(event));

        //Assert
        List<Event> finalEvents = eventManager.getAllEvents(PLANNER_ID);
        assertEquals("Expected count of existing events after delete should be 5", 5, finalEvents.size());
        assertFalse("The existing events should not include the event", finalEvents.contains(event));

    }

    @Test
    public void testSetSortParameterIT_and_getAllEvents_sortedByName() {
        // Arrange
        eventManager.addNewEvent(PLANNER_ID, EVENT2_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);

        // Act
        eventManager.setSortParameter("Name");
        List<Event> sortedEvents = eventManager.getAllEvents(PLANNER_ID);

        // Assert
        assertEquals("events not sorted correctly", EVENT1_NAME_STRING, sortedEvents.get(0).getEventName());
        assertEquals("events not sorted correctly", EVENT2_NAME_STRING, sortedEvents.get(1).getEventName());
    }

    @Test
    public void testSetSortParameterIT_and_getAllEvents_sortedByEventDate() {// Arrange
        //Arrange
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE_LATER, EVENT_TIME);
        eventManager.addNewEvent(PLANNER_ID, EVENT1_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME);

        // Act
        eventManager.setSortParameter("Event Date");
        List<Event> sortedEvents = eventManager.getAllEvents(PLANNER_ID);

        // Assert
        assertEquals("events not sorted correctly", PARSED_DATE, sortedEvents.get(0).getEventDate());
        assertEquals("events not sorted correctly", PARSED_DATE_LATER, sortedEvents.get(1).getEventDate());
    }

    @After
    public void tearDown() {
        // reset DB
        tempDB.delete();
    }

}
