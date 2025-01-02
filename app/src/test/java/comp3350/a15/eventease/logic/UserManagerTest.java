package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import comp3350.a15.eventease.logic.exceptions.NewPasswordMissingLowercaseException;
import comp3350.a15.eventease.logic.exceptions.UserAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.UserNotFoundException;
import comp3350.a15.eventease.logic.implementation.UserManagerImpl;
import comp3350.a15.eventease.objects.User;
import comp3350.a15.eventease.persistence.IUserPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

@RunWith(MockitoJUnitRunner.class)
public class UserManagerTest {
    private static final String USER_NAME = "JohnDoe";
    private static final String USER_LOGIN = "johndoe";
    private static final String USER_PASSWORD = "Password@123";
    private static final boolean IS_VENDOR = true;

    @Mock
    private IUserPersistence userPersistence;

    private UserManagerImpl userManager;

    @Before
    public void setUp() {
        userManager = new UserManagerImpl(userPersistence);
    }

    @Test
    public void testLoginUser_Success() throws UserNotFoundException {
        // Arrange
        User mockUser = new User(USER_NAME, USER_LOGIN, USER_PASSWORD, IS_VENDOR);
        when(userPersistence.getUserByUsername(USER_LOGIN)).thenReturn(mockUser);

        // Act
        userManager.loginUser(USER_LOGIN, USER_PASSWORD);

        // Assert
        verify(userPersistence).getUserByUsername(USER_LOGIN);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginUser_UserNotFound() throws UserNotFoundException {
        // Arrange
        when(userPersistence.getUserByUsername(USER_LOGIN)).thenReturn(null);

        // Act
        userManager.loginUser(USER_LOGIN, USER_PASSWORD);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginUser_IncorrectPassword() throws UserNotFoundException {
        // Arrange
        User mockUser = new User(USER_NAME, USER_LOGIN, "IncorrectPassword", IS_VENDOR);
        when(userPersistence.getUserByUsername(USER_LOGIN)).thenReturn(mockUser);

        // Act
        userManager.loginUser(USER_LOGIN, USER_PASSWORD);
    }

    @Test
    public void testIsUserVendor_True() {
        // Arrange
        User mockUser = new User(USER_NAME, USER_LOGIN, USER_PASSWORD, IS_VENDOR);
        when(userPersistence.getUserByUsername(USER_LOGIN)).thenReturn(mockUser);

        // Act
        boolean result = userManager.isUserVendor(USER_LOGIN);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsUserVendor_False() {
        // Arrange
        User mockUser = new User(USER_NAME, USER_LOGIN, USER_PASSWORD, false);
        when(userPersistence.getUserByUsername(USER_LOGIN)).thenReturn(mockUser);

        // Act
        boolean result = userManager.isUserVendor(USER_LOGIN);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRegisterUser_Success() throws UserAlreadyExistsException {
        // Arrange
        when(userPersistence.getUserByUsername(USER_LOGIN)).thenReturn(null);
        when(userPersistence.addNewUser(any(User.class))).thenReturn(true);

        // Act & Assert
        userManager.registerUser(USER_NAME, USER_LOGIN, USER_PASSWORD, USER_PASSWORD, IS_VENDOR);
        verify(userPersistence).addNewUser(any(User.class));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testRegisterUser_AlreadyExists() throws UserAlreadyExistsException {
        // Arrange
        when(userPersistence.getUserByUsername(USER_LOGIN)).thenReturn(new User(USER_NAME, USER_LOGIN, USER_PASSWORD, IS_VENDOR));

        // Act
        userManager.registerUser(USER_NAME, USER_LOGIN, USER_PASSWORD, USER_PASSWORD, IS_VENDOR);
    }

    @Test(expected = NewPasswordMissingLowercaseException.class)
    public void testRegisterUser_PasswordMissingLowercase() throws UserAlreadyExistsException {
        // Arrange
        final String passwordWithoutLowercase = "PASSWORD@123";

        // Act
        userManager.registerUser(USER_NAME, USER_LOGIN, passwordWithoutLowercase, passwordWithoutLowercase, IS_VENDOR);
    }

    @Test
    public void testGetUserId_Success() {
        // Arrange
        final int expectedUserId = 1;
        when(userPersistence.getUserIdByUsername(USER_LOGIN)).thenReturn(expectedUserId);

        // Act
        int userId = userManager.getUserId(USER_LOGIN);

        // Assert
        assertEquals(expectedUserId, userId);
        verify(userPersistence).getUserIdByUsername(USER_LOGIN);
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange
        when(userPersistence.deleteUser(anyString())).thenReturn(true);

        // Act
        userManager.deleteUser(USER_LOGIN);

        // Assert
        verify(userPersistence).deleteUser(USER_LOGIN);
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeleteUser_Failure() {
        // Arrange
        when(userPersistence.deleteUser(anyString())).thenReturn(false);

        // Act
        userManager.deleteUser(USER_LOGIN);
    }


    @Test
    public void testGetUserByUsername_Success() {
        // Arrange
        when(userPersistence.getUserByUsername(anyString())).thenReturn(
                new User("John Doe", "John", "John123@!", true));

        // Act
        userManager.getUserbyUsername(USER_LOGIN);

        // Assert
        verify(userPersistence).getUserByUsername(USER_LOGIN);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserByUsername_Failure() {
        // Arrange
        when(userPersistence.getUserByUsername(anyString())).thenThrow(PersistenceException.class);

        // Act
        userManager.getUserbyUsername(USER_LOGIN);
    }
}
