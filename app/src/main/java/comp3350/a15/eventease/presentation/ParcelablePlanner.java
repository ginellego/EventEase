package comp3350.a15.eventease.presentation;

import android.os.Parcel;
import android.os.Parcelable;

import comp3350.a15.eventease.logic.IPlannerManager;
import comp3350.a15.eventease.objects.Planner;

public class ParcelablePlanner implements Parcelable {
    private final int plannerId;
    private final String firstname;
    private final String lastname;
    private final String phone;
    private final String email;
    private final String bio;

    public ParcelablePlanner(Planner toCopy) {
        this.plannerId = toCopy.getId();
        this.firstname = toCopy.getFirstname();
        this.lastname = toCopy.getLastname();
        this.phone = toCopy.getPhoneNumber();
        this.email = toCopy.getEmail();
        this.bio = toCopy.getBio();
    }

    protected ParcelablePlanner(Parcel in) {
        plannerId = in.readInt();
        firstname = in.readString();
        lastname = in.readString();
        phone = in.readString();
        email = in.readString();
        bio = in.readString();
    }

    public Planner toPlanner(IPlannerManager plannerManager) {
        return plannerManager.getPlannerByUserId(plannerId);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(plannerId);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(bio);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable CREATOR
    public static final Creator<ParcelablePlanner> CREATOR = new Creator<ParcelablePlanner>() {
        @Override
        public ParcelablePlanner createFromParcel(Parcel in) {
            return new ParcelablePlanner(in);
        }

        @Override
        public ParcelablePlanner[] newArray(int size) {
            return new ParcelablePlanner[size];
        }
    };
}