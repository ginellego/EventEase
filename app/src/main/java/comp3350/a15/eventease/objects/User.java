package comp3350.a15.eventease.objects;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

public class User {
    private final String name;
    private final String username;
    private final String password;
    private final boolean isVendor;

    @AssistedInject
    public User(@Assisted("name") String name, @Assisted("login") String username,
                @Assisted("password") String password, @Assisted("isVendor") boolean isVendor) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.isVendor = isVendor;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isVendor() {
        return isVendor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;
        return name.equals(user.name) && username.equals(user.username)
                && password.equals(user.password) && isVendor == user.isVendor;
    }
}
