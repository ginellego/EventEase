package comp3350.a15.eventease.objects.factory.implementation;

import javax.inject.Inject;

import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.objects.factory.IVendorFactory;

public class VendorFactoryImpl implements IVendorFactory {
    @Inject
    public VendorFactoryImpl () {
    }

    @Override
    public Vendor create(int accountId, String name, String serviceType, String description,
                         String phoneNumber, String email, int cost, String rating,
                         int picture, int capacity) {
        return new Vendor(accountId, name, serviceType, description, phoneNumber, email,
                cost, rating, picture, capacity);
    }

}
