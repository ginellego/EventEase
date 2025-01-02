package comp3350.a15.eventease.objects.factory;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import comp3350.a15.eventease.objects.Event;

public interface IEventFactory {
    Event create(int plannerId, String name, String description,
                 String location, LocalDate date, LocalTime time,
                 Timestamp whenCreated);
}
