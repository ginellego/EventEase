package comp3350.a15.eventease.objects;

import java.util.Objects;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

public class Vendor {

    private final int vendorPictures;
    private final int accountId;
    private final int vendorId;
    private final String vendorName;
    private final String serviceType;
    private final String description;
    private final String phoneNumber;
    private final String email;
    private final int cost;
    private final String rating;
    private final int capacity;


    @AssistedInject
    public Vendor(@Assisted("accountId") int accountId, @Assisted("name") String name, @Assisted("serviceType") String serviceType,
                  @Assisted("description") String description, @Assisted("phoneNumber") String phoneNumber,
                  @Assisted("email") String email, @Assisted("cost") int cost, @Assisted("rating") String rating,
                  @Assisted("picture") int picture, @Assisted("capacity") int capacity) {
        this.accountId = accountId;
        vendorName = name;
        this.serviceType = serviceType;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.cost = cost;
        this.rating = rating;
        this.vendorPictures = picture;
        this.capacity = capacity;
        this.vendorId = hashCode();

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                accountId,
                vendorName,
                serviceType,
                phoneNumber,
                email
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vendor vendor = (Vendor) o;
        return vendorId == vendor.vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorDescription() {
        return description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getVendorNumber() {
        return phoneNumber;
    }

    public String getVendorEmail() {
        return email;
    }

    public String getRating() {
        return rating;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getVendorPictures() {
        return vendorPictures;
    }

    public int getCost() {
        return cost;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getAccountId() {
        return accountId;
    }
}