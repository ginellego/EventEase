package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import comp3350.a15.eventease.TestUtils;
import comp3350.a15.eventease.logic.exceptions.EmptyVendorCategoryException;
import comp3350.a15.eventease.logic.exceptions.VendorNotAddedDuplicateVendorException;
import comp3350.a15.eventease.logic.exceptions.VendorNotFoundException;
import comp3350.a15.eventease.logic.implementation.UserManagerImpl;
import comp3350.a15.eventease.logic.implementation.VendorManagerImpl;
import comp3350.a15.eventease.objects.User;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.objects.factory.IVendorFactory;
import comp3350.a15.eventease.objects.factory.implementation.EventFactoryImpl;
import comp3350.a15.eventease.objects.factory.implementation.VendorFactoryImpl;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.IUserPersistence;
import comp3350.a15.eventease.persistence.IVendorPersistence;
import comp3350.a15.eventease.persistence.hsqldb.EventPersistenceImplHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.VendorPersistenceHSQLDB;

@RunWith(JUnit4.class)

public class VendorManagerIT {
    private static final String USER1_NAME = "JohnDoe";
    private static final String USER1_USERNAME = "johndoe";
    private static final String USER1_PASSWORD = "Password@123";
    private static final String USER2_NAME = "JohnWick";
    private static final String USER2_USERNAME = "johnwick";
    private static final String USER2_PASSWORD = "Password@123";
    private static final boolean IS_PLANNER = false;
    private static final boolean IS_VENDOR = true;
    @Mock
    private IUserPersistence userPersistence;

    private UserManagerImpl userManager;
    private IVendorPersistence vendorPersistence;
    private VendorManagerImpl vendorManager;

    private IEventManager eventManager;

    private File tempDB;

    private static final int PLANNER_ID = 0;
    private static final Timestamp TIME_STAMP = new Timestamp(System.currentTimeMillis());
    private static final CharSequence EVENT1_NAME = "An Event1";
    private static final String EVENT1_NAME_STRING = "An Event1";
    private static final CharSequence EVENT2_NAME = "An Event2";
    private static final String EVENT2_NAME_STRING = "An Event2";
    private static final CharSequence EVENT_DESCRIPTION = "Test Description";
    private static final String EVENT_DESCRIPTION_STRING = "Test Description";
    private static final CharSequence EVENT_LOCATION = "Test Location";
    private static final String EVENT_LOCATION_STRING = "Test Location";
    private static final CharSequence EVENT_DATE = LocalDate.now().plusDays(1).toString();
    private static final LocalDate PARSED_DATE = LocalDate.parse(EVENT_DATE);
    private static final CharSequence EVENT_DATE_LATER = LocalDate.now().plusDays(2).toString();
    private static final LocalDate PARSED_DATE_LATER = LocalDate.parse(EVENT_DATE_LATER);
    private static final CharSequence EVENT_TIME = "00:00";
    private static final LocalTime PARSED_TIME = LocalTime.parse(EVENT_TIME);

    public static final int VENDOR_ACCOUNT_ID_1 = 1;
    public static final String VENDOR_NAME_1 = "Vendor1";
    public static final String VENDOR_SERVICE_TYPE_1 = "Catering";
    public static final String VENDOR_DESCRIPTION_1 = "Providing catering services for events";
    public static final String VENDOR_PHONE_NUMBER_1 = "123-456-7890";
    public static final String VENDOR_EMAIL_1 = "vendor1@example.com";
    public static final int VENDOR_COST_1 = 200;
    public static final String VENDOR_RATING_1 = "4.5 Stars";
    public static final int VENDOR_PICTURE_1 = 0;
    public static final int VENDOR_CAPACITY_1 = 150;

    public static final int VENDOR_ACCOUNT_ID_2 = 2;
    public static final String VENDOR_NAME_2 = "Vendor2";
    public static final String VENDOR_SERVICE_TYPE_2 = "Venue";
    public static final String VENDOR_DESCRIPTION_2 = "Providing venue";
    public static final String VENDOR_PHONE_NUMBER_2 = "987-654-3210";
    public static final String VENDOR_EMAIL_2 = "vendor2@example.com";
    public static final int VENDOR_COST_2 = 300;
    public static final String VENDOR_RATING_2 = "5 Stars";
    public static final int VENDOR_PICTURE_2 = 1;
    public static final int VENDOR_CAPACITY_2 = 200;
    public static final int VENDOR_ID_INVALID = -1;

    public static final int VENDOR_ACCOUNT_ID_INVALID = -999;

    public static final String VENDOR_SERVICE_TYPE_3 = "Band";
    public static final String VENDOR_SERVICE_TYPE_4 = "Decor";

    public static final int VENDOR_DATABASE_DEFAULT_NUM_ENTRIES = 1;

    @Before
    public void testSetup() throws IOException {
        tempDB = TestUtils.copyDB();
        IVendorFactory vendorFactory = new VendorFactoryImpl();
        IEventFactory eventFactory = new EventFactoryImpl();
        vendorPersistence = new VendorPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        IEventPersistence eventPersistence = new EventPersistenceImplHSQLDB("testDB", eventFactory);
        vendorManager = new VendorManagerImpl(vendorPersistence);
        userPersistence = new UserPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        userManager = new UserManagerImpl(userPersistence);
    }

    @Test
    public void testGetAllVendors_Success() {
        User newUser1 = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_VENDOR);
        User newUser2 = new User(USER2_NAME, USER2_USERNAME, USER2_PASSWORD, IS_VENDOR);
        userPersistence.addNewUser(newUser1);
        userPersistence.addNewUser(newUser2);
        int userId1 = userManager.getUserId(USER1_USERNAME);
        int userId2 = userManager.getUserId(USER2_USERNAME);
        // Arrange
        Vendor vendor1 = new Vendor(userId1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);

        Vendor vendor2 = new Vendor(userId2, VENDOR_NAME_2, VENDOR_SERVICE_TYPE_2,
                VENDOR_DESCRIPTION_2, VENDOR_PHONE_NUMBER_2,
                VENDOR_EMAIL_2, VENDOR_COST_2,
                VENDOR_RATING_2, VENDOR_PICTURE_2,
                VENDOR_CAPACITY_2);

        vendorPersistence.addNewVendor(vendor1);
        vendorPersistence.addNewVendor(vendor2);
        List<Vendor> initialVendors = vendorPersistence.getAllVendors();

        // Act
        List<Vendor> retrievedVendors = vendorManager.getAllVendors();

        // Assert
        assertNotNull(retrievedVendors);
        assertFalse(retrievedVendors.isEmpty());
        assertEquals(initialVendors.size(), retrievedVendors.size());
    }

    @Test
    public void testAddNewVendor_Success() {
        // Arrange
        ArrayList<Vendor> initialVendors = vendorPersistence.getAllVendors();
        Vendor vendor1 = new Vendor(VENDOR_ACCOUNT_ID_1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);
        // Act
        vendorManager.addNewVendor(vendor1);
        ArrayList<Vendor> updatedVendors = vendorPersistence.getAllVendors();

        // Assert
        assertTrue(updatedVendors.contains(vendor1));
        assertEquals(initialVendors.size() + 1, updatedVendors.size());
    }

    @Test(expected = VendorNotAddedDuplicateVendorException.class)
    public void testAddNewVendor_Failure() {
        // Arrange
        Vendor vendor1 = new Vendor(VENDOR_ACCOUNT_ID_1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);
        vendorPersistence.addNewVendor(vendor1);
        Vendor existingVendor = vendorPersistence.getAllVendors().get(0);

        // Act
        vendorManager.addNewVendor(existingVendor);
    }

    @Test
    public void testGetVendorFromPersistence_Success() {
        // Arrange
        Vendor vendor1 = new Vendor(VENDOR_ACCOUNT_ID_1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);
        vendorPersistence.addNewVendor(vendor1);
        Vendor expectedVendor = vendorPersistence.getAllVendors().get(0);

        // Act
        Vendor retrievedVendor = vendorManager.getVendorFromPersistence(expectedVendor.getVendorId());

        // Assert
        assertNotNull(retrievedVendor);
        assertEquals(expectedVendor, retrievedVendor);
    }

    @Test(expected = VendorNotFoundException.class)
    public void testGetVendorFromPersistence_NotFound() {
        // Act
        vendorManager.getVendorFromPersistence(VENDOR_ID_INVALID);
    }

    @Test
    public void testGetVendorByAccountId_Success() {
        Vendor vendor1 = new Vendor(VENDOR_ACCOUNT_ID_1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);
        vendorPersistence.addNewVendor(vendor1);
        Vendor expectedVendor = vendorPersistence.getAllVendors().get(0);

        // Act
        Vendor retrievedVendor = vendorManager.getVendorByAccountId(VENDOR_ACCOUNT_ID_1);

        // Assert
        assertNotNull(retrievedVendor);
        assertEquals(expectedVendor, retrievedVendor);
    }

    @Test(expected = VendorNotFoundException.class)
    public void testGetVendorByAccountId_NotFound() {
        // Act
        vendorManager.getVendorByAccountId(VENDOR_ACCOUNT_ID_INVALID);
    }

    @Test
    public void testGetVendorsByServiceType_Success() {
        // Arrange
        User newUser1 = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_VENDOR);
        User newUser2 = new User(USER2_NAME, USER2_USERNAME, USER2_PASSWORD, IS_VENDOR);
        userPersistence.addNewUser(newUser1);
        userPersistence.addNewUser(newUser2);
        int userId1 = userManager.getUserId(USER1_USERNAME);
        int userId2 = userManager.getUserId(USER2_USERNAME);
        Vendor vendor1 = new Vendor(userId1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);

        Vendor vendor2 = new Vendor(userId2, VENDOR_NAME_2, VENDOR_SERVICE_TYPE_2,
                VENDOR_DESCRIPTION_2, VENDOR_PHONE_NUMBER_2,
                VENDOR_EMAIL_2, VENDOR_COST_2,
                VENDOR_RATING_2, VENDOR_PICTURE_2,
                VENDOR_CAPACITY_2);

        vendorPersistence.addNewVendor(vendor1);
        vendorPersistence.addNewVendor(vendor2);
        // Act
        List<Vendor> expectedVendors1 = vendorManager.getVendorsbyServiceType(VENDOR_SERVICE_TYPE_1);
        List<Vendor> expectedVendors2 = vendorManager.getVendorsbyServiceType(VENDOR_SERVICE_TYPE_2);

        // Assert
        assertEquals(1, expectedVendors1.size());
        assertEquals(vendor1, expectedVendors1.get(0));

        assertEquals(1, expectedVendors2.size());
        assertEquals(vendor2, expectedVendors2.get(0));

    }

    @Test
    public void testGetVendorsByServiceType_EmptyList() {
        // Arrange
        Vendor vendor1 = new Vendor(VENDOR_ACCOUNT_ID_1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);

        vendorPersistence.addNewVendor(vendor1);

        // Act
        List<Vendor> listOfVendors = vendorManager.getVendorsbyServiceType(VENDOR_SERVICE_TYPE_2);

        //Assert
        assertEquals("The list should be empty", new ArrayList<>(), listOfVendors);
    }

    @Test
    public void testGetMultipleCategories_Success() {
        // Arrange
        User newUser1 = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_VENDOR);
        User newUser2 = new User(USER2_NAME, USER2_USERNAME, USER2_PASSWORD, IS_VENDOR);
        userPersistence.addNewUser(newUser1);
        userPersistence.addNewUser(newUser2);
        int userId1 = userManager.getUserId(USER1_USERNAME);
        int userId2 = userManager.getUserId(USER2_USERNAME);
        Vendor vendor1 = new Vendor(userId1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);

        Vendor vendor2 = new Vendor(userId2, VENDOR_NAME_2, VENDOR_SERVICE_TYPE_2,
                VENDOR_DESCRIPTION_2, VENDOR_PHONE_NUMBER_2,
                VENDOR_EMAIL_2, VENDOR_COST_2,
                VENDOR_RATING_2, VENDOR_PICTURE_2,
                VENDOR_CAPACITY_2);
        ArrayList<String> serviceTypes = new ArrayList<>();
        serviceTypes.add(VENDOR_SERVICE_TYPE_1);
        serviceTypes.add(VENDOR_SERVICE_TYPE_2);

        vendorPersistence.addNewVendor(vendor1);
        vendorPersistence.addNewVendor(vendor2);

        // Act
        ArrayList<Vendor> vendors = vendorManager.getMultipleCategories(serviceTypes);

        // Assert
        assertEquals(2, vendors.size());
        assertTrue(vendors.contains(vendor1));
        assertTrue(vendors.contains(vendor2));
    }

    @Test(expected = EmptyVendorCategoryException.class)
    public void testGetMultipleCategories_EmptyResult() {
        // Arrange
        User newUser1 = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_VENDOR);
        User newUser2 = new User(USER2_NAME, USER2_USERNAME, USER2_PASSWORD, IS_VENDOR);
        userPersistence.addNewUser(newUser1);
        userPersistence.addNewUser(newUser2);
        int userId1 = userManager.getUserId(USER1_USERNAME);
        int userId2 = userManager.getUserId(USER2_USERNAME);
        // Arrange
        Vendor vendor1 = new Vendor(userId1, VENDOR_NAME_1, VENDOR_SERVICE_TYPE_1,
                VENDOR_DESCRIPTION_1, VENDOR_PHONE_NUMBER_1,
                VENDOR_EMAIL_1, VENDOR_COST_1,
                VENDOR_RATING_1, VENDOR_PICTURE_1,
                VENDOR_CAPACITY_1);

        Vendor vendor2 = new Vendor(userId2, VENDOR_NAME_2, VENDOR_SERVICE_TYPE_2,
                VENDOR_DESCRIPTION_2, VENDOR_PHONE_NUMBER_2,
                VENDOR_EMAIL_2, VENDOR_COST_2,
                VENDOR_RATING_2, VENDOR_PICTURE_2,
                VENDOR_CAPACITY_2);
        ArrayList<String> serviceTypes = new ArrayList<>();
        serviceTypes.add(VENDOR_SERVICE_TYPE_3);
        serviceTypes.add(VENDOR_SERVICE_TYPE_4);

        vendorPersistence.addNewVendor(vendor1);
        vendorPersistence.addNewVendor(vendor2);

        // Act
        vendorManager.getMultipleCategories(serviceTypes);
    }

    @After
    public void tearDown() {
        // reset DB
        tempDB.delete();
    }
}
