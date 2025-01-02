package comp3350.a15.eventease.logic.implementation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.exceptions.CantModifyEventException;
import comp3350.a15.eventease.logic.exceptions.EventAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.EventDateInThePastException;
import comp3350.a15.eventease.logic.exceptions.EventNotAddedException;
import comp3350.a15.eventease.logic.exceptions.EventNotDeletedException;
import comp3350.a15.eventease.logic.exceptions.EventNotFoundException;
import comp3350.a15.eventease.logic.exceptions.FieldMissingException;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

public class EventManagerImpl implements IEventManager {
    private final IEventPersistence eventPersistence;
    private final IEventFactory eventFactory;
    private String sortBy = "Date Added(old -> new)";

    @Inject
    public EventManagerImpl(IEventPersistence eventPersistence, IEventFactory eventFactory
    ) {
        this.eventPersistence = eventPersistence;
        this.eventFactory = eventFactory;
    }

    @Override
    public List<Event> getAllEvents(int id) {
        return sort(eventPersistence.getAllEvents(id));
    }

    @Override
    public Event addNewEvent(int plannerId, CharSequence eventName, CharSequence eventDescription, CharSequence eventLocation,
                             CharSequence eventDate, CharSequence eventTime) {
        Event addedEvent;

        validateAllFieldsPresent(eventName, eventDescription, eventLocation,
                eventDate, eventTime);

        boolean eventAdded;
        addedEvent = makeEvent(plannerId, eventName, eventDescription, eventLocation, eventDate, eventTime);
        checkEventDateValid(addedEvent.getEventDate());
        checkDuplicateEvent(addedEvent);
        eventAdded = eventPersistence.addNewEvent(addedEvent);

        if (!eventAdded) {
            throw new EventNotAddedException("Error adding event to database");
        }

        return addedEvent;
    }

    private static void validateAllFieldsPresent(CharSequence... fields) {
        boolean allFieldsPresent = true;
        for (CharSequence field : fields) {
            if (field == null || field.toString().trim().isEmpty()) {
                allFieldsPresent = false;
            }

            if (!allFieldsPresent) {
                throw new FieldMissingException("Fields Missing");
            }
        }
    }

    private Event makeEvent(int plannerId, CharSequence eventName, CharSequence eventDescription, CharSequence eventLocation,
                            CharSequence eventDate, CharSequence eventTime) {

        //Formatting of date and time information is for conversion to the correct format for storage in the event database
        //not for presentation to the user
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(eventDate, dateFormat);
        LocalTime time = parseMixedTime(eventTime.toString());

        return eventFactory.create(plannerId, eventName.toString(), eventDescription.toString(),
                eventLocation.toString(), date, time, new Timestamp(System.currentTimeMillis()));
    }

    private static void checkEventDateValid(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new EventDateInThePastException("date is in the past");
        }
    }

    private void checkDuplicateEvent(Event addedEvent) {
        if (eventPersistence.doesEventExist(addedEvent)) {
            throw new EventAlreadyExistsException("Event already exists");
        }
    }

    private static LocalTime parseMixedTime(String timeString) throws DateTimeParseException {
        DateTimeFormatter[] timeFormatters = {
                DateTimeFormatter.ofPattern("HH:mm"),
                DateTimeFormatter.ofPattern("h:mma"),
                DateTimeFormatter.ofPattern("h:mm a"),
                DateTimeFormatter.ofPattern("HH:mm:ss"),
                DateTimeFormatter.ofPattern("h:mm[:ss] a")
        };

        for (DateTimeFormatter formatter : timeFormatters) {
            try {
                return LocalTime.parse(timeString, formatter);
            } catch (DateTimeParseException e) {
                // Try the next formatter if the current one fails
            }
        }

        // If none of the formatters succeed, throw an exception
        throw new DateTimeParseException("Unable to parse time: " + timeString, timeString, 0);
    }

    @Override
    public void deleteEvent(Event event) {
        // Event exists, proceed with deletion
        boolean eventDeleted = eventPersistence.deleteEvent(event);
        if (!eventDeleted) {
            throw new EventNotDeletedException("Event could not be deleted");
        }
    }

    @Override
    public Event getEvent(int eventId, int plannerId) {
        try {
            return eventPersistence.getEvent(eventId, plannerId);
        } catch (PersistenceException e) {
            throw new EventNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public Event editEvent(Event eventToEdit, CharSequence name, CharSequence description, CharSequence location, CharSequence date, CharSequence time) {
        validateAllFieldsPresent(name, description, location,
                date, time);

        Event editedEvent = makeEvent(eventToEdit.getPlannerId(), name, description, location, date, time);
        checkEventDateValid(editedEvent.getEventDate());
        checkDuplicateEvent(editedEvent);
        try {
            eventPersistence.saveEdit(eventToEdit, editedEvent);
        } catch (PersistenceException e) {
            throw new CantModifyEventException(e);
        }
        return editedEvent;
    }

    @Override
    public void setSortParameter(String parameter) {
        sortBy = parameter;
    }

    private List<Event> sort(List<Event> events) {
        List<Event> sortedEvents = new ArrayList<>(events);
        switch (sortBy) {
            case "Event Date":
                // Sort by date and then by time
                sortedEvents.sort(Comparator.comparing(Event::getEventDate)
                        .thenComparing(Event::getEventTime));
                break;
            case "Name":
                // Sort by event name
                sortedEvents.sort(Comparator.comparing(Event::getEventName, String.CASE_INSENSITIVE_ORDER));
                break;
            case "Date Added(new -> old)":
                // Sort by when the event was created
                sortedEvents.sort(Comparator.comparing(Event::getWhenCreated).reversed());
                break;
            case "Date Added(old -> new)":
                // Sort by when the event was created
                sortedEvents.sort(Comparator.comparing(Event::getWhenCreated));
                break;
            case "(none)":
            default:
                break;
        }

        return sortedEvents;
    }
}
