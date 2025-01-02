package comp3350.a15.eventease.objects.factory;

import comp3350.a15.eventease.objects.Planner;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface IPlannerFactory {
    Planner create(@Assisted("userid") int userId, @Assisted("firstname") String firstname, @Assisted("lastname") String lastname,
                   @Assisted("phone") String phone, @Assisted("email") String email, @Assisted("rating") float rating, @Assisted("bio") String bio);
}
