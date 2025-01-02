package comp3350.a15.eventease.logic.implementation;

import javax.inject.Inject;
import javax.inject.Singleton;

import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.logic.exceptions.NewPasswordDoesNotMatchException;
import comp3350.a15.eventease.logic.exceptions.NewPasswordMissingLowercaseException;
import comp3350.a15.eventease.logic.exceptions.NewPasswordMissingNumberException;
import comp3350.a15.eventease.logic.exceptions.NewPasswordMissingSpecialCharException;
import comp3350.a15.eventease.logic.exceptions.NewPasswordMissingUppercaseException;
import comp3350.a15.eventease.logic.exceptions.NewPasswordTooShortException;
import comp3350.a15.eventease.logic.exceptions.UserAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.UserException;
import comp3350.a15.eventease.logic.exceptions.UserNotFoundException;
import comp3350.a15.eventease.objects.User;
import comp3350.a15.eventease.persistence.IUserPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

@Singleton
public class UserManagerImpl implements IUserManager {
    private final IUserPersistence userPersistence;

    @Inject
    public UserManagerImpl(IUserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @Override
    public User loginUser(String username, String password) throws UserNotFoundException {
        User user;
        try {
            user = userPersistence.getUserByUsername(username);
        } catch (PersistenceException e) {
            throw new UserNotFoundException("Incorrect username or password");
        }
        if (user == null || !user.getPassword().equals(password)) {
            throw new UserNotFoundException("Incorrect username or password");
        }

        return user;
    }

    @Override
    public boolean isUserVendor(String username) {
        User existingUser = userPersistence.getUserByUsername(username);
        return existingUser.isVendor();
    }

    @Override
    public User createUser(String name, String username, String password, String passwordRepeat, boolean isVendor) {
        User existingUser = userPersistence.getUserByUsername(username);
        if (existingUser != null) {
            throw new UserAlreadyExistsException("This username is already taken...");
        }

        validateNewPassword(password, passwordRepeat);

        return new User(name, username, password, isVendor);
    }

    @Override
    public void registerUser(String name, String username, String password, String passwordRepeat, boolean isVendor) {
        User newUser = createUser(name, username, password, passwordRepeat, isVendor);
        if (!userPersistence.addNewUser(newUser)) {
            throw new UserException("Could not add user");
        }
    }

    private void validateNewPassword(String password, String passwordRepeat) {
        int minPasswordLength = 7;

        boolean containsLowerCaseChar = false;
        boolean containsUpperCaseChar = false;
        boolean containsNumber = false;
        boolean containsSpecialChar = false;

        if (!password.equals(passwordRepeat))
            throw new NewPasswordDoesNotMatchException("Password fields do not match");
        if (password.length() < minPasswordLength)
            throw new NewPasswordTooShortException("Password contains too few characters, should be a minimum of" + minPasswordLength + "characters long");

        String specialCharacters = "!@#$%^&*(),.?\\\":{}|<>_";
        char curr;
        for (int i = 0; i < password.length(); i++) {
            curr = password.charAt(i);

            if (Character.isUpperCase(curr))
                containsUpperCaseChar = true;
            else if (Character.isLowerCase(curr))
                containsLowerCaseChar = true;
            else if (Character.isDigit(curr))
                containsNumber = true;
            else if (specialCharacters.indexOf(curr) != -1)
                containsSpecialChar = true;
        }

        if (!containsLowerCaseChar)
            throw new NewPasswordMissingLowercaseException("Password should have at least one lowercase letter");
        if (!containsUpperCaseChar)
            throw new NewPasswordMissingUppercaseException("Password should have at least one uppercase letter");
        if (!containsNumber)
            throw new NewPasswordMissingNumberException("Password should have at least one number");
        if (!containsSpecialChar)
            throw new NewPasswordMissingSpecialCharException("Password should have at least one special character");
    }

    @Override
    public int getUserId(String username) {
        return userPersistence.getUserIdByUsername(username);
    }

    @Override
    public void deleteUser(String username) {
        if (!userPersistence.deleteUser(username)) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public User getUserbyUsername(String username) {
        try {
            return userPersistence.getUserByUsername(username);
        } catch (PersistenceException e) {
            throw new UserNotFoundException();
        }
    }
}
