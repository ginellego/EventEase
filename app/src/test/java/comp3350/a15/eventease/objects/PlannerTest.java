package comp3350.a15.eventease.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PlannerTest {
    @Test
    public void testPlanner() {
        //Arrange
        final int accountId = 0;
        final String firstname = "John";
        final String lastname = "Doe";
        final String phoneNumber = "12345678";
        final String email = "JohnDoe@example.com";
        final float rating = 3.5f;
        final String bio = "This is my bio";

        //Act
        Planner planner = new Planner(accountId, firstname, lastname, phoneNumber, email, rating, bio);

        // Assert
        assertNotNull("Planner should not be null", planner);
        assertEquals("Unexpected planner account ID", accountId, planner.getAccountId());
        assertEquals("Unexpected planner firstname", firstname, planner.getFirstname());
        assertEquals("Unexpected planner lastname", lastname, planner.getLastname());
        assertEquals("Unexpected planner phone number", phoneNumber, planner.getPhoneNumber());
        assertEquals("Unexpected planner email", email, planner.getEmail());
        assertEquals(rating, planner.getRating(), 0.05);
        assertEquals("Unexpected planner bio", bio, planner.getBio());
    }
}
