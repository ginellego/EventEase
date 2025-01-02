package comp3350.a15.eventease.objects;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event {
    private final int id;
    private final int plannerId;
    private final String eventName;
    private final String eventDescription;
    private final String eventLocation;
    private final LocalDate eventDate;
    private final LocalTime eventTime;
    private final Timestamp whenCreated;

    public Event(int plannerId, String name, String description,
                 String location, LocalDate date, LocalTime time,
                 Timestamp whenCreated) {
        this.plannerId = plannerId;
        eventName = name;
        eventDescription = description;
        eventLocation = location;
        eventDate = date;
        eventTime = time;
        this.whenCreated = whenCreated;
        id = hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(plannerId, eventName, eventDescription, eventLocation, eventDate, eventTime);
    }

    //Event specific equals method which checks equality of event fields
    //Rudimentary logic provided in object so that fake database used in testing can compare objects
    //It makes sense to avoid use of logic layer in searching for objects in the data layer
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return id == ((Event) obj).getId();
    }

    public int getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public int getPlannerId() {
        return plannerId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

}
