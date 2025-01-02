package comp3350.a15.eventease.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UserTest {

    @Test
    public void testUser() {
        // Arrange
        final String userName = "TestUser";
        final String name = "John Doe";
        final String password = "Password@1";
        final boolean isVendor = false;

        // Act
        User user = new User(name, userName, password, isVendor);

        // Assert
        assertNotNull("User should not be null", user);
        assertEquals("User name should match", name, user.getName());
        assertEquals("Username should match", userName, user.getUsername());
        assertEquals("Password should match", password, user.getPassword());
        assertEquals("isVendor should match", isVendor, user.isVendor());
    }

    @Test
    public void testUserEquality() {
        // Arrange
        User user1 = new User("John Doe", "testuser", "Password@1", false);
        User user2 = new User("John Doe", "testuser", "Password@1", false);
        User user3 = new User("Jane Smith", "janesmith", "@1Password", true);

        // Act & Assert
        // Check if user1 is equal to user2
        assertEquals("User1 should be equal to User2", user1, user2);
        // Check if user1 is not equal to user3
        assertNotEquals("User1 should not be equal to User3", user1, user3);
    }
}
