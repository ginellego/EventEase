package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import comp3350.a15.eventease.logic.exceptions.EmptyVendorCategoryException;
import comp3350.a15.eventease.logic.exceptions.VendorDatabaseFetchException;
import comp3350.a15.eventease.logic.exceptions.VendorNotAddedException;
import comp3350.a15.eventease.logic.exceptions.VendorNotFoundException;
import comp3350.a15.eventease.logic.implementation.VendorManagerImpl;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.persistence.IVendorPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

@RunWith(MockitoJUnitRunner.class)
public class VendorManagerTest {

    @Mock
    private IVendorPersistence vendorPersistence;

    private VendorManagerImpl vendorManager;

    @Before
    public void setUp() {
        vendorManager = new VendorManagerImpl(vendorPersistence);
    }

    @Test
    public void testGetAllVendors_Success() throws PersistenceException {
        ArrayList<Vendor> expectedVendors = new ArrayList<>();
        expectedVendors.add(new Vendor(1, "VendorName", "ServiceType", "Description", "PhoneNumber", "Email", 100, "5 Stars", 0, 150));
        when(vendorPersistence.getAllVendors()).thenReturn(expectedVendors);

        ArrayList<Vendor> vendors = vendorManager.getAllVendors();

        assertNotNull(vendors);
        assertFalse(vendors.isEmpty());
        assertEquals(expectedVendors.size(), vendors.size());
    }

    @Test(expected = VendorDatabaseFetchException.class)
    public void testGetAllVendors_Failure() throws PersistenceException {
        when(vendorPersistence.getAllVendors()).thenThrow(new VendorDatabaseFetchException("Cannot get vendors from the database"));

        vendorManager.getAllVendors();
    }

    @Test
    public void testAddNewVendor_Success() throws PersistenceException {
        Vendor newVendor = new Vendor(2, "NewVendor", "NewServiceType", "NewDescription", "NewPhoneNumber", "NewEmail", 200, "4 Stars", 0, 200);
        when(vendorPersistence.getAllVendors()).thenReturn(new ArrayList<>());
        when(vendorPersistence.addNewVendor(any(Vendor.class))).thenReturn(true);

        vendorManager.addNewVendor(newVendor);

        verify(vendorPersistence).addNewVendor(any(Vendor.class));
    }

    @Test(expected = VendorNotAddedException.class)
    public void testAddNewVendor_Failure() throws PersistenceException {
        Vendor newVendor = new Vendor(3, "FailedVendor", "FailedService", "FailedDescription", "FailedNumber", "FailedEmail", 300, "3 Stars", 0, 250);
        when(vendorPersistence.getAllVendors()).thenReturn(new ArrayList<>());
        doThrow(new PersistenceException("Vendor not added")).when(vendorPersistence).addNewVendor(any(Vendor.class));

        vendorManager.addNewVendor(newVendor);
        verify(vendorPersistence).addNewVendor(newVendor);
    }

    @Test
    public void testGetVendorFromPersistence_Success() throws PersistenceException {
        int vendorId = 1;
        Vendor expectedVendor = new Vendor(4, "ExpectedVendor", "ExpectedServiceType", "ExpectedDescription", "ExpectedPhoneNumber", "ExpectedEmail", 400, "5 Stars", 0, 300);
        when(vendorPersistence.getVendorById(vendorId)).thenReturn(expectedVendor);

        Vendor vendor = vendorManager.getVendorFromPersistence(vendorId);

        assertNotNull(vendor);
        assertEquals(expectedVendor, vendor);
    }

    @Test(expected = VendorNotFoundException.class)
    public void testGetVendorFromPersistence_NotFound() throws PersistenceException {
        int vendorId = 1;
        when(vendorPersistence.getVendorById(vendorId)).thenThrow(new VendorNotFoundException("Vendor could not be found"));

        vendorManager.getVendorFromPersistence(vendorId);

        verify(vendorPersistence).getVendorById(vendorId);
    }

    // Continuing from the previous VendorManagerTest

    @Test
    public void testGetVendorByAccountId_Success() throws PersistenceException {
        int accountId = 2;
        Vendor expectedVendor = new Vendor(accountId, "VendorName", "ServiceType", "Description", "PhoneNumber", "Email", 100, "5 Stars", 0, 150);
        when(vendorPersistence.getVendorByAccountId(accountId)).thenReturn(expectedVendor);

        Vendor vendor = vendorManager.getVendorByAccountId(accountId);

        assertNotNull(vendor);
        assertEquals(expectedVendor, vendor);
    }

    @Test(expected = VendorNotFoundException.class)
    public void testGetVendorByAccountId_NotFound() throws PersistenceException {
        int accountId = 2;
        when(vendorPersistence.getVendorByAccountId(accountId)).thenThrow(new PersistenceException("Vendor could not be found"));

        vendorManager.getVendorByAccountId(accountId);

        verify(vendorPersistence).getVendorByAccountId(accountId);
    }

    @Test
    public void testGetVendorsByServiceType_Success() throws PersistenceException {
        String serviceType = "Catering";
        List<Vendor> expectedVendors = new ArrayList<>();
        expectedVendors.add(new Vendor(3, "VendorName", serviceType, "Description", "PhoneNumber", "Email", 200, "4 Stars", 0, 100));
        when(vendorPersistence.getVendorByServiceType(serviceType)).thenReturn(expectedVendors);

        List<Vendor> vendors = vendorManager.getVendorsbyServiceType(serviceType);

        assertNotNull(vendors);
        assertFalse(vendors.isEmpty());
        assertEquals(expectedVendors.size(), vendors.size());
    }

    @Test
    public void testGetVendorsByServiceType_EmptyList() throws PersistenceException {
        String serviceType = "Catering";
        when(vendorPersistence.getVendorByServiceType(serviceType)).thenThrow(new EmptyVendorCategoryException());

        List<Vendor> vendors = vendorManager.getVendorsbyServiceType(serviceType);

        assertNotNull(vendors);
        assertTrue(vendors.isEmpty());
    }

    @Test
    public void testGetMultipleCategories_Success() throws PersistenceException {
        ArrayList<String> serviceTypes = new ArrayList<>();
        serviceTypes.add("Catering");
        serviceTypes.add("Photography");

        ArrayList<Vendor> expectedVendors = new ArrayList<>();
        expectedVendors.add(new Vendor(4, "VendorName1", "Catering", "Description", "PhoneNumber", "Email", 300, "5 Stars", 0, 200));
        expectedVendors.add(new Vendor(5, "VendorName2", "Photography", "Description", "PhoneNumber", "Email", 400, "4.5 Stars", 0, 100));

        when(vendorPersistence.getVendorByServiceType("Catering")).thenReturn(new ArrayList<>(expectedVendors.subList(0, 1)));
        when(vendorPersistence.getVendorByServiceType("Photography")).thenReturn(new ArrayList<>(expectedVendors.subList(1, 2)));

        ArrayList<Vendor> vendors = vendorManager.getMultipleCategories(serviceTypes);

        assertNotNull(vendors);
        assertFalse(vendors.isEmpty());
        assertEquals(2, vendors.size());
    }

    @Test(expected = EmptyVendorCategoryException.class)
    public void testGetMultipleCategories_EmptyResult() throws PersistenceException {
        ArrayList<String> serviceTypes = new ArrayList<>();
        serviceTypes.add("Catering");
        serviceTypes.add("Photography");

        when(vendorPersistence.getVendorByServiceType("Catering")).thenReturn(new ArrayList<>());
        when(vendorPersistence.getVendorByServiceType("Photography")).thenReturn(new ArrayList<>());

        vendorManager.getMultipleCategories(serviceTypes);
    }

    @Test(expected = PersistenceException.class)
    public void testGetVendorByServiceType_DatabaseError() throws PersistenceException {
        String serviceType = "Entertainment";
        when(vendorPersistence.getVendorByServiceType(serviceType)).thenThrow(new PersistenceException("Database fetch error"));

        vendorManager.getVendorsbyServiceType(serviceType);
    }

    @Test
    public void testGetMultipleCategories_WithOneEmptyResult() throws PersistenceException {
        ArrayList<String> serviceTypes = new ArrayList<>();
        serviceTypes.add("Music");
        serviceTypes.add("Decoration");

        ArrayList<Vendor> musicVendors = new ArrayList<>();
        musicVendors.add(new Vendor(7, "MusicVendor", "Music", "Description", "PhoneNumber", "Email", 600, "4 Stars", 0, 400));

        when(vendorPersistence.getVendorByServiceType("Music")).thenReturn(musicVendors);
        when(vendorPersistence.getVendorByServiceType("Decoration")).thenReturn(new ArrayList<>());

        ArrayList<Vendor> vendors = vendorManager.getMultipleCategories(serviceTypes);

        assertNotNull(vendors);
        assertFalse(vendors.isEmpty());
        assertEquals(1, vendors.size()); // Expect only the Music vendors to be returned
    }

    @Test(expected = VendorDatabaseFetchException.class)
    public void testGetAllVendors_UnhandledDatabaseError() throws PersistenceException {
        when(vendorPersistence.getAllVendors()).thenThrow(new PersistenceException("Unhandled database error"));

        vendorManager.getAllVendors();
    }

    @Test
    public void testAddNewVendor_WhenListContainsSimilarButNotDuplicate() throws PersistenceException {
        Vendor similarVendor = new Vendor(8, "SimilarVendor", "SimilarService", "Description", "PhoneNumber", "Email", 700, "4.5 Stars", 0, 450);
        ArrayList<Vendor> existingVendors = new ArrayList<>();
        existingVendors.add(similarVendor);

        Vendor newVendor = new Vendor(9, "NewSimilarVendor", "SimilarService", "Description", "PhoneNumber", "Email", 700, "4.5 Stars", 0, 450);

        when(vendorPersistence.getAllVendors()).thenReturn(existingVendors);
        when(vendorPersistence.addNewVendor(newVendor)).thenReturn(true);

        vendorManager.addNewVendor(newVendor);
        verify(vendorPersistence).addNewVendor(newVendor); // Verify that the addNewVendor was called despite the similar existing vendor
    }

    @Test(expected = VendorNotFoundException.class)
    public void testGetVendorFromPersistence_DatabaseError() throws PersistenceException {
        int vendorId = 10;
        when(vendorPersistence.getVendorById(vendorId)).thenThrow(new PersistenceException("Database error"));

        vendorManager.getVendorFromPersistence(vendorId);
    }


}
