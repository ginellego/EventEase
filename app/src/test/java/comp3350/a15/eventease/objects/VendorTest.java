package comp3350.a15.eventease.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VendorTest {

    @Test
    public void testVendor() {
        //Arrange
        final int vendorPicture = 1;
        final int vendorAccountId = 1;
        final String vendorName = "Vendor Name";
        final String vendorServiceType = "Cake";
        final String vendorDescription = "Vendor Description";
        final String vendorPhoneNumber = "204-204-2410";
        final String vendorEmail = "vendor@vedor.com";
        final int vendorCost = 100;
        final String vendorRating = "4.2";
        final int vendorCapacity = 100;

        //Act
        Vendor vendor = new Vendor(vendorAccountId, vendorName, vendorServiceType, vendorDescription, vendorPhoneNumber, vendorEmail, vendorCost, vendorRating, vendorPicture, vendorCapacity);

        //Assert
        assertNotNull("Vendor should not be null", vendor);
        assertEquals("Vendor picture should match", vendorPicture, vendor.getVendorPictures());
        assertEquals("Vendor account ID should match", vendorAccountId, vendor.getAccountId());
        assertEquals("Vendor name should match", vendorName, vendor.getVendorName());
        assertEquals("Vendor service type should match", vendorServiceType, vendor.getServiceType());
        assertEquals("Vendor description should match", vendorDescription, vendor.getVendorDescription());
        assertEquals("Vendor phone number should match", vendorPhoneNumber, vendor.getVendorNumber());
        assertEquals("Vendor email should match", vendorEmail, vendor.getVendorEmail());
        assertEquals("Vendor cost should match", vendorCost, vendor.getCost());
        assertEquals("Vendor rating should match", vendorRating, vendor.getRating());
        assertEquals("Vendor capacity should match", vendorCapacity, vendor.getCapacity());
    }

    @Test
    public void testVendorsNotEqual() {
        // Arrange
        Vendor vendor1 = new Vendor(1, "Vendor1", "ServiceType1", "Description1", "PhoneNumber1", "Email1", 100, "5 Stars", 0, 150);
        Vendor vendor2 = null;

        // Act & Assert
        assertNotEquals("Vendors should not be equal", vendor1, vendor2);
    }

    @Test
    public void testVendorsEqual() {
        // Arrange
        Vendor vendor1 = new Vendor(1, "Vendor1", "ServiceType1", "Description1", "PhoneNumber1", "Email1", 100, "5 Stars", 0, 150);
        Vendor vendor2 = new Vendor(1, "Vendor1", "ServiceType1", "Description1", "PhoneNumber1", "Email1", 100, "5 Stars", 0, 150);

        // Act & Assert
        assertEquals("Vendors should be equal", vendor1, vendor2);
    }

    @Test
    public void testVendorEqualToSelf() {
        Vendor vendor1 = new Vendor(1, "Vendor1", "ServiceType1", "Description1", "PhoneNumber1", "Email1", 100, "5 Stars", 0, 150);

        assertEquals("Vendor should be equal to self", vendor1, vendor1);
    }
}
