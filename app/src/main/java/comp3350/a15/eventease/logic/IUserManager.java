package comp3350.a15.eventease.logic;

import comp3350.a15.eventease.logic.exceptions.UserNotFoundException;
import comp3350.a15.eventease.objects.User;

public interface IUserManager {
    User loginUser(String username, String password) throws UserNotFoundException;

    boolean isUserVendor(String username);

    User createUser(String name, String username, String password, String passwordRepeat, boolean isVendor);

    void registerUser(String name, String username, String password, String passwordRepeat, boolean isVendor);

    int getUserId(String username);

    User getUserbyUsername(String username);

    void deleteUser(String username);
}
