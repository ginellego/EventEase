package comp3350.a15.eventease.persistence_stub;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.persistence.IVendorPersistence;

public class VendorPersistenceStub implements IVendorPersistence {
    private final ArrayList<Vendor> vendors;

    @Inject
    public VendorPersistenceStub() {
        vendors = new ArrayList<>();
    }

    @Override
    public ArrayList<Vendor> getAllVendors() {
        return vendors;
    }

    @Override
    public boolean addNewVendor(Vendor newVendor) {
        if (newVendor != null && !vendors.contains(newVendor)) {
            return vendors.add(newVendor);
        }
        return false;
    }

    @Override
    public boolean doesVendorExist(Vendor vendor) {
        return vendors.contains(vendor);
    }

    @Override
    public Vendor getVendorById(int vendorId) {
        return vendors.stream()
                .filter(vendor -> vendor.getVendorId() == vendorId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Vendor getVendorByAccountId(int accountId) {
        return vendors.stream()
                .filter(vendor -> vendor.getVendorId() == accountId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Vendor> getVendorByServiceType(String serviceType) {
        return vendors.stream().filter(vendor -> vendor.getServiceType().equals(serviceType)).collect(Collectors.toList());
    }
}
