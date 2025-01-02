package comp3350.a15.eventease.persistence_stub;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import comp3350.a15.eventease.objects.User;
import comp3350.a15.eventease.persistence.IUserPersistence;

@Singleton
public class UserPersistenceImpl implements IUserPersistence {
    private final ArrayList<User> users;

    @Inject
    public UserPersistenceImpl() {
        this.users = new ArrayList<>();
    }

    @Override
    public boolean addNewUser(User newUser) {
        // Implement logic to add a new user (consider uniqueness of login), to be done later
        return users.add(newUser);
    }

    @Override
    public User getUserByUsername(String username) {
        // Implement logic to retrieve a user by login
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public int getUserIdByUsername(String username) {
        // this method is used only in HSQLDB
        // for this implementation we want it just as a mock
        return 0;
    }

    @Override
    public boolean deleteUser(String userName) {
        return users.removeIf(user -> user.getUsername().equals(userName));
    }
}
