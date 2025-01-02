package comp3350.a15.eventease.presentation;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;

import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.objects.ServiceRequest;

public class ParcelableServiceRequest implements Parcelable {
    public static final Creator<ParcelableServiceRequest> CREATOR = new Creator<ParcelableServiceRequest>() {
        @Override
        public ParcelableServiceRequest createFromParcel(Parcel in) {
            return new ParcelableServiceRequest(in);
        }

        @Override
        public ParcelableServiceRequest[] newArray(int size) {
            return new ParcelableServiceRequest[size];
        }
    };
    private final int id;
    private final int associatedEventId;
    private final int plannerId;
    private final int vendorId;
    private final String serviceType;
    private final LocalDate deadline;
    private final long budget;
    private final ServiceRequest.ServiceStatus serviceStatus;

    public ParcelableServiceRequest(ServiceRequest toCopy) {
        id = toCopy.getId();
        associatedEventId = toCopy.getAssociatedEvent().getId();
        plannerId = toCopy.getAssociatedEvent().getPlannerId();
        vendorId = toCopy.getVendorID();
        serviceType = toCopy.getServiceType();
        deadline = toCopy.getDeadline();
        budget = toCopy.getBudget();
        serviceStatus = toCopy.getServiceStatus();
    }

    public int getId() {
        return id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public int getPlannerId() {
        return plannerId;
    }

    protected ParcelableServiceRequest(Parcel in) {
        id = in.readInt();
        associatedEventId = in.readInt();
        plannerId = in.readInt();
        vendorId = in.readInt();
        serviceType = in.readString();
        deadline = in.readSerializable(LocalDate.class.getClassLoader(), LocalDate.class);
        budget = in.readLong();
        serviceStatus = in.readSerializable(ServiceRequest.ServiceStatus.class.getClassLoader(), ServiceRequest.ServiceStatus.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(associatedEventId);
        parcel.writeInt(plannerId);
        parcel.writeInt(vendorId);
        parcel.writeString(serviceType);
        parcel.writeSerializable(deadline);
        parcel.writeLong(budget);
        parcel.writeSerializable(serviceStatus);
    }

    public ServiceRequest toServiceRequest(IServiceRequestManager serviceRequestManager) {
        return serviceRequestManager.getRequest(id, vendorId);
    }

    public enum ServiceStatus {
        NEW,
        PENDING,

        ACCEPTED,
        REJECTED,
    }

}
