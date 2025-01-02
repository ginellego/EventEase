package comp3350.a15.eventease.presentation;


import android.os.Parcel;
import android.os.Parcelable;

import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.objects.Vendor;

public class ParcelableVendor implements Parcelable {
    // Parcelable CREATOR
    public static final Creator<ParcelableVendor> CREATOR = new Creator<ParcelableVendor>() {
        @Override
        public ParcelableVendor createFromParcel(Parcel in) {
            return new ParcelableVendor(in);
        }

        @Override
        public ParcelableVendor[] newArray(int size) {
            return new ParcelableVendor[size];
        }
    };
    private final int vendorId;
    private final int accountId;
    private final String vendorName;
    private final String serviceTypes;
    private final String description;
    private final String phoneNumber;
    private final String email;
    private final int cost;
    private final String rating;
    private final int vendorPictures;
    private final int capacity;

    public ParcelableVendor(Vendor toCopy) {
        vendorId = toCopy.getVendorId();
        accountId = toCopy.getAccountId();
        vendorName = toCopy.getVendorName();
        serviceTypes = toCopy.getServiceType();
        description = toCopy.getVendorDescription();
        phoneNumber = toCopy.getVendorNumber();
        email = toCopy.getVendorEmail();
        cost = toCopy.getCost();
        rating = toCopy.getRating();
        vendorPictures = toCopy.getVendorPictures();
        capacity = toCopy.getCapacity();
    }

    protected ParcelableVendor(Parcel in) {
        vendorId = in.readInt();
        accountId = in.readInt();
        vendorName = in.readString();
        serviceTypes = in.readString();
        description = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        cost = in.readInt();
        rating = in.readString();
        vendorPictures = in.readInt();
        capacity = in.readInt();
    }

    public Vendor toVendor(IVendorManager vendorManager) {
        return vendorManager.getVendorFromPersistence(vendorId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vendorId);
        dest.writeInt(accountId);
        dest.writeString(vendorName);
        dest.writeString(serviceTypes);
        dest.writeString(description);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeInt(cost);
        dest.writeString(rating);
        dest.writeInt(vendorPictures);
        dest.writeInt(capacity);
    }
}

