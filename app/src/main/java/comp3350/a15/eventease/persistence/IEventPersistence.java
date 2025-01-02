package comp3350.a15.eventease.persistence;

import java.util.List;

import comp3350.a15.eventease.objects.Event;

public interface IEventPersistence {
    List<Event> getAllEvents(int plannerId);
    boolean addNewEvent(Event newEvent);
    boolean doesEventExist(Event event);
    boolean deleteEvent(Event event);
    Event getEvent(int eventId, int plannerId);

    void saveEdit(Event eventToEdit, Event eventPostEdit) ;
}
