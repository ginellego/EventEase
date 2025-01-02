package comp3350.a15.eventease.logic.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.logic.exceptions.EmptyVendorCategoryException;
import comp3350.a15.eventease.logic.exceptions.VendorDatabaseFetchException;
import comp3350.a15.eventease.logic.exceptions.VendorNotAddedDuplicateVendorException;
import comp3350.a15.eventease.logic.exceptions.VendorNotAddedException;
import comp3350.a15.eventease.logic.exceptions.VendorNotFoundException;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.persistence.IVendorPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

public class VendorManagerImpl implements IVendorManager {

    private final IVendorPersistence vendorPersistence;

    @Inject
    public VendorManagerImpl(IVendorPersistence vendorPersistence) {
        this.vendorPersistence = vendorPersistence;
    }

    @Override
    public ArrayList<Vendor> getAllVendors() {
        try {
            return vendorPersistence.getAllVendors();
        } catch (PersistenceException e) {
            throw new VendorDatabaseFetchException("Cannot get vendors from the database");
        }
    }

    @Override
    public void addNewVendor(Vendor newVendor) {
        ArrayList<Vendor> allvendors;
        try {
            allvendors = vendorPersistence.getAllVendors();
        } catch (PersistenceException e) {
            throw new VendorDatabaseFetchException("Cannot get vendors from the database");
        }

        if (allvendors.contains(newVendor)) {
            throw new VendorNotAddedDuplicateVendorException("Vendor could not be added since vendor already exists in the database");
        }
        boolean vendorAdded;
        try {
            vendorAdded = vendorPersistence.addNewVendor(newVendor);
        } catch (PersistenceException e) {
            throw new VendorNotAddedException("Vendor could not be added to the database");
        }

        if (!vendorAdded) {
            throw new VendorNotAddedException("Vendor could not be added");
        }
    }

    @Override
    public Vendor getVendorFromPersistence(int vendorId) {
        try {
            return vendorPersistence.getVendorById(vendorId);
        } catch (PersistenceException e) {
            throw new VendorNotFoundException("Vendor could not be found");
        }
    }

    @Override
    public Vendor getVendorByAccountId(int userId) {
        try {
            return vendorPersistence.getVendorByAccountId(userId);
        } catch (PersistenceException e) {
            throw new VendorNotFoundException("Vendor could not be found");
        }
    }

    @Override
    public List<Vendor> getVendorsbyServiceType(String serviceType) {
        try {
            return vendorPersistence.getVendorByServiceType(serviceType);
        } catch (EmptyVendorCategoryException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<Vendor> getMultipleCategories(ArrayList<String> serviceType) {
        ArrayList<Vendor> categoryVendors = new ArrayList<>();
        for (String type : serviceType) {
            categoryVendors.addAll(getVendorsbyServiceType(type));
        }
        if (categoryVendors.isEmpty()) {
            throw new EmptyVendorCategoryException();
        }
        return categoryVendors;
    }
}
