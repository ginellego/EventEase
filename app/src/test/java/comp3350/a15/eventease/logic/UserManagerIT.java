package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import comp3350.a15.eventease.TestUtils;
import comp3350.a15.eventease.logic.exceptions.EventNotFoundException;
import comp3350.a15.eventease.logic.exceptions.NewPasswordMissingLowercaseException;
import comp3350.a15.eventease.logic.exceptions.UserAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.UserNotFoundException;
import comp3350.a15.eventease.logic.implementation.EventManagerImpl;
import comp3350.a15.eventease.logic.implementation.UserManagerImpl;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.User;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.objects.factory.implementation.EventFactoryImpl;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.IUserPersistence;
import comp3350.a15.eventease.persistence.hsqldb.EventPersistenceImplHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.UserPersistenceHSQLDB;

@RunWith(JUnit4.class)

public class UserManagerIT {
    private static final String USER1_NAME = "JohnDoe";
    private static final String USER1_USERNAME = "johndoe";
    private static final String USER1_PASSWORD = "Password@123";
    private static final String USER2_NAME = "JohnWick";
    private static final String USER2_USERNAME = "johnwick";
    private static final String USER2_PASSWORD = "Password@123";
    private static final boolean IS_VENDOR = true;
    private static final boolean IS_PLANNER = false;
    private static final String EVENT_NAME = "Test Event";
    private static final String EVENT_DESCRIPTION = "Test Description";
    private static final String EVENT_LOCATION = "Test Location";
    private static final LocalDate EVENT_DATE = LocalDate.now().plusDays(1);
    private static final LocalTime EVENT_TIME = LocalTime.of(10, 0);
    private static IUserManager userManager;
    private File tempDB;
    private IUserPersistence userPersistence;
    private IEventManager eventManager;
    private IEventPersistence eventPersistence;

    @Before
    public void setUp() throws IOException {
        tempDB = TestUtils.copyDB();
        IEventFactory eventFactory = new EventFactoryImpl();
        userPersistence = new UserPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        eventPersistence = new EventPersistenceImplHSQLDB(tempDB.getAbsolutePath().replace(".script", ""), eventFactory);
        userManager = new UserManagerImpl(userPersistence);
        eventManager = new EventManagerImpl(eventPersistence, eventFactory);
    }

    @Test
    public void testUserAccessToEvent_Success() throws UserNotFoundException, EventNotFoundException {
        // Arrange
        User newUser = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_PLANNER);
        userPersistence.addNewUser(newUser);
        int userId = userManager.getUserId(USER1_USERNAME);
        Event newEvent = new Event(userId, EVENT_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME, new Timestamp(System.currentTimeMillis()));
        eventPersistence.addNewEvent(newEvent);

        // Act
        userManager.loginUser(USER1_USERNAME, USER1_PASSWORD);
        Event retrievedEvent = eventManager.getEvent(newEvent.getId(), userId);

        // Assert
        assertNotNull("The event should exist", retrievedEvent);
        assertEquals("The plannerId should match the userId", userId, retrievedEvent.getPlannerId());
        assertEquals("The retrieved event should be same with newEvent", newEvent, retrievedEvent);
    }

    @Test(expected = EventNotFoundException.class)
    public void testUserAccessToUnavailableEvent_Failure() throws UserNotFoundException, EventNotFoundException {
        // Arrange
        User newUser = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_PLANNER);
        userPersistence.addNewUser(newUser);
        int userId = userManager.getUserId(USER1_USERNAME);
        Event newEvent = new Event(userId, EVENT_NAME, EVENT_DESCRIPTION, EVENT_LOCATION, EVENT_DATE, EVENT_TIME, new Timestamp(System.currentTimeMillis()));
        eventPersistence.addNewEvent(newEvent);


        // Act
        userManager.loginUser(USER1_USERNAME, USER1_PASSWORD);
        Event retrievedEvent = eventManager.getEvent(123456, userId); // Invalid event ID

        // Assert
        assertNull("The event should not exist", retrievedEvent);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginUser_UserNotFound() throws UserNotFoundException {
        // Act
        userManager.loginUser("random_username", "random_password");
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginUser_IncorrectPassword() throws UserNotFoundException {
        // Arrange
        User newUser = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_VENDOR);
        userPersistence.addNewUser(newUser);

        // Act
        userManager.loginUser(USER1_USERNAME, "IncorrectPassword");
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testRegisterUser_AlreadyExists() throws UserAlreadyExistsException {
        // Arrange
        User newUser = new User(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, IS_PLANNER);
        userPersistence.addNewUser(newUser);

        // Act
        userManager.registerUser(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, USER1_PASSWORD, IS_VENDOR);
    }

    @Test(expected = NewPasswordMissingLowercaseException.class)
    public void testRegisterUser_PasswordMissingLowercase() throws UserAlreadyExistsException {
        // Arrange
        final String passwordWithoutLowercase = "PASSWORD@123";

        // Act
        userManager.registerUser(USER1_NAME, USER1_USERNAME, passwordWithoutLowercase, passwordWithoutLowercase, IS_VENDOR);
    }

    @Test
    public void testGetUserId_Success() throws UserAlreadyExistsException {
        // Arrange
        userManager.registerUser(USER1_NAME, USER1_USERNAME, USER1_PASSWORD, USER1_PASSWORD, IS_VENDOR);
        userManager.registerUser(USER2_NAME, USER2_USERNAME, USER2_PASSWORD, USER2_PASSWORD, IS_PLANNER);

        // Act
        int userId1 = userManager.getUserId(USER1_USERNAME);
        int userId2 = userManager.getUserId(USER2_USERNAME);

        // Assert
        assertEquals("User id is an IDENTITY field so the first test user should have userId 2", 2, userId1);
        assertEquals("User id is an IDENTITY field so the second test user should have userId 3", 3, userId2);
    }

    @After
    public void tearDown() {
        // reset DB
        tempDB.delete();
    }
}
