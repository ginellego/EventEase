package comp3350.a15.eventease.persistence_stub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import comp3350.a15.eventease.logic.exceptions.EventNotFoundException;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.persistence.IEventPersistence;

@Singleton
public class EventPersistenceStub implements IEventPersistence {
    private final ArrayList<Event> events;

    @Inject
    public EventPersistenceStub() {
        events = new ArrayList<>();
    }

    @Override
    public List<Event> getAllEvents(int plannerId) {
        return Collections.unmodifiableList(events);
    }

    @Override
    public boolean addNewEvent(Event newEvent) {
        if (newEvent != null && !events.contains(newEvent)) {
            return events.add(newEvent);
        }
        return false;
    }

    @Override
    public boolean doesEventExist(Event event) {
        return events.contains(event);
    }

    @Override
    public boolean deleteEvent(Event event) {
        return events.remove(event);
    }

    @Override
    public Event getEvent(int position, int plannerId) {
        try {
            return events.get(position);
        } catch (IndexOutOfBoundsException e) {
            throw new EventNotFoundException("Event could not be found.");
        }
    }

    @Override
    public void saveEdit(Event eventToEdit, Event eventPostEdit) {
        int position = events.indexOf(eventToEdit);
        if (position >= 0) {
            events.set(position, eventPostEdit);
        }
    }

}
