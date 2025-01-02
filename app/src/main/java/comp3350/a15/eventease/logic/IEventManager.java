package comp3350.a15.eventease.logic;

import java.util.List;

import comp3350.a15.eventease.objects.Event;

public interface IEventManager {
    List<Event> getAllEvents(int plannerId);
        Event addNewEvent(int plannerId, CharSequence eventName, CharSequence eventDescription, CharSequence eventLocation,
                          CharSequence eventDate, CharSequence eventTime);

    void deleteEvent(Event event);

    Event getEvent(int eventId, int plannerId);

    Event editEvent(Event event, CharSequence name, CharSequence description, CharSequence location, CharSequence date, CharSequence time);

    void setSortParameter(String parameter);
}
