package comp3350.a15.eventease.persistence;

import comp3350.a15.eventease.objects.User;

public interface IUserPersistence {
    boolean addNewUser(User newUser);

    User getUserByUsername(String username);

    int getUserIdByUsername(String username);

    boolean deleteUser(String userName);
}
