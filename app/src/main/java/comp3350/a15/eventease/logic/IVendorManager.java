package comp3350.a15.eventease.logic;

import java.util.ArrayList;
import java.util.List;

import comp3350.a15.eventease.objects.Vendor;

public interface IVendorManager {
    ArrayList<Vendor> getAllVendors();

    void addNewVendor(Vendor newVendor);

    Vendor getVendorFromPersistence(int vendorId);

    Vendor getVendorByAccountId(int userId);

    List<Vendor> getVendorsbyServiceType(String serviceType);

    ArrayList<Vendor> getMultipleCategories(ArrayList<String> serviceType);

}
