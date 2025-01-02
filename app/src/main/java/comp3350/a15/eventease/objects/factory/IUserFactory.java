package comp3350.a15.eventease.objects.factory;

import comp3350.a15.eventease.objects.User;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface IUserFactory {
    User create(@Assisted("name") String name,
                @Assisted("login") String login,
                @Assisted("password") String password,
                @Assisted("isVendor") boolean isVendor);
}
