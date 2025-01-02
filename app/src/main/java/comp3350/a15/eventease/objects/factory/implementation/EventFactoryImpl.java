package comp3350.a15.eventease.objects.factory.implementation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.inject.Inject;

import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.factory.IEventFactory;

public class EventFactoryImpl implements IEventFactory {
    @Inject
    public EventFactoryImpl() {
    }

    @Override
    public Event create(int plannerId, String name, String description,
                        String location, LocalDate date, LocalTime time, Timestamp whenCreated) {
        return new Event(plannerId, name, description, location, date, time, whenCreated);
    }
}
