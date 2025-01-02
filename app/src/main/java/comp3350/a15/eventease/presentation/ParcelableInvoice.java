package comp3350.a15.eventease.presentation;

import android.os.Parcel;
import android.os.Parcelable;

import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.objects.Invoice;

public class ParcelableInvoice implements Parcelable {
    // Parcelable CREATOR
    public static final Creator<ParcelableInvoice> CREATOR = new Creator<>() {
        @Override
        public ParcelableInvoice createFromParcel(Parcel in) {
            return new ParcelableInvoice(in);
        }

        @Override
        public ParcelableInvoice[] newArray(int size) {
            return new ParcelableInvoice[size];
        }
    };
    private final int id;
    private final int requestId;
    private final int plannerId;
    private final String serviceType;
    private final String eventName;
    private final String eventDate;
    private final String eventTime;
    private final String eventLocation;
    private final long offerAccepted;

    public ParcelableInvoice(Invoice toCopy) {
        id = toCopy.getId();
        requestId = toCopy.getRequestId();
        plannerId = toCopy.getPlannerId();
        serviceType = toCopy.getServiceType();
        eventName = toCopy.getEventName();
        eventDate = toCopy.getEventDate();
        eventTime = toCopy.getEventTime();
        eventLocation = toCopy.getEventLocation();
        offerAccepted = toCopy.getOfferAccepted();
    }

    protected ParcelableInvoice(Parcel in) {
        id = in.readInt();
        requestId = in.readInt();
        plannerId = in.readInt();
        serviceType = in.readString();
        eventName = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();
        eventLocation = in.readString();
        offerAccepted = in.readLong();
    }

    public String getServiceType() {
        return serviceType;
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

    public Invoice toInvoice(IRequestInvoiceManager invoiceManager) {
        return invoiceManager.getInvoice(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(requestId);
        dest.writeInt(plannerId);
        dest.writeString(serviceType);
        dest.writeString(eventName);
        dest.writeString(eventDate);
        dest.writeString(eventTime);
        dest.writeString(eventLocation);
        dest.writeLong(offerAccepted);
    }
}
