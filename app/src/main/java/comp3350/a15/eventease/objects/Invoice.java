package comp3350.a15.eventease.objects;

import java.util.Objects;

public class Invoice {
    private final int requestId;
    private final int plannerId;
    private final String serviceType;
    private final String eventName;
    private final String eventDate;
    private final String eventTime;
    private final String eventLocation;
    private final long offerAccepted;
    private final int id;


    public Invoice(int requestId, int plannerId,
                   String serviceType, String eventName,
                   String eventDate, String eventTime,
                   String eventLocation, long offerAccepted) {
        this.requestId = requestId;
        this.plannerId = plannerId;
        this.serviceType = serviceType;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.offerAccepted = offerAccepted;
        id = hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(plannerId, requestId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        return id == invoice.id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public int getPlannerId() {
        return plannerId;
    }

    public int getId() {
        return id;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public long getOfferAccepted() {
        return offerAccepted;
    }
}
