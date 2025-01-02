package comp3350.a15.eventease.persistence;

import java.util.ArrayList;
import java.util.List;

import comp3350.a15.eventease.objects.Vendor;

public interface IVendorPersistence {
    ArrayList<Vendor> getAllVendors();

    boolean addNewVendor(Vendor newVendor);

    boolean doesVendorExist(Vendor vendor);

    Vendor getVendorById(int vendorId);

    Vendor getVendorByAccountId(int accountId);


    List<Vendor> getVendorByServiceType(String serviceType);
}
