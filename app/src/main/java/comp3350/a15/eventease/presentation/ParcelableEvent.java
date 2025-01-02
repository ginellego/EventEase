package comp3350.a15.eventease.presentation;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.objects.Event;

public class ParcelableEvent implements Parcelable {
    // Parcelable CREATOR
    public static final Creator<ParcelableEvent> CREATOR = new Creator<ParcelableEvent>() {
        @Override
        public ParcelableEvent createFromParcel(Parcel in) {
            return new ParcelableEvent(in);
        }

        @Override
        public ParcelableEvent[] newArray(int size) {
            return new ParcelableEvent[size];
        }
    };
    private final int id;
    private final int plannerId;
    private final String eventName;
    private final String eventDescription;
    private final String eventLocation;
    private final LocalDate eventDate;
    private final LocalTime eventTime;
    private final Timestamp whenCreated;

    public ParcelableEvent(Event toCopy) {
        id = toCopy.getId();
        plannerId = toCopy.getPlannerId();
        eventName = toCopy.getEventName();
        eventDescription = toCopy.getEventDescription();
        eventLocation = toCopy.getEventLocation();
        eventDate = toCopy.getEventDate();
        eventTime = toCopy.getEventTime();
        whenCreated = toCopy.getWhenCreated();
    }

    protected ParcelableEvent(Parcel in) {
        id = in.readInt();
        plannerId = in.readInt();
        eventName = in.readString();
        eventDescription = in.readString();
        eventLocation = in.readString();
        eventDate = in.readSerializable(LocalDate.class.getClassLoader(), LocalDate.class);
        eventTime = in.readSerializable(LocalTime.class.getClassLoader(), LocalTime.class);
        whenCreated = in.readSerializable(Timestamp.class.getClassLoader(), Timestamp.class);
    }

    public Event toEvent(IEventManager eventManager) {
        return eventManager.getEvent(id, plannerId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(plannerId);
        dest.writeString(eventName);
        dest.writeString(eventDescription);
        dest.writeString(eventLocation);
        dest.writeSerializable(eventDate);
        dest.writeSerializable(eventTime);
        dest.writeSerializable(whenCreated);
    }
}
