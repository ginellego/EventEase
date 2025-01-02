package comp3350.a15.eventease.presentation;

import android.os.Parcel;
import android.os.Parcelable;

import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.objects.User;

public class ParcelableUser implements Parcelable {
    private final String name;
    private final String username;
    private final String password;
    private final boolean isVendor;


    public ParcelableUser(User toCopy) {
        this.name = toCopy.getName();
        this.username = toCopy.getUsername();
        this.password = toCopy.getPassword();
        this.isVendor = toCopy.isVendor();

    }


    protected ParcelableUser(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        isVendor = in.readBoolean();

    }

    public User toUser(IUserManager userManager) {
        return userManager.getUserbyUsername(this.username);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeBoolean(isVendor);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable CREATOR
    public static final Creator<ParcelableUser> CREATOR = new Creator<ParcelableUser>() {
        @Override
        public ParcelableUser createFromParcel(Parcel in) {
            return new ParcelableUser(in);
        }

        @Override
        public ParcelableUser[] newArray(int size) {
            return new ParcelableUser[0];
        }

    };

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isVendor() {
        return isVendor;
    }
}
