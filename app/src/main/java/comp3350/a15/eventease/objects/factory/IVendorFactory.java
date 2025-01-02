package comp3350.a15.eventease.objects.factory;

import comp3350.a15.eventease.objects.Vendor;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;

@AssistedFactory
public interface IVendorFactory {
    Vendor create(@Assisted("accountId") int accountId, @Assisted("name") String name, @Assisted("serviceType") String serviceType,
                  @Assisted("description") String description, @Assisted("phoneNumber") String phoneNumber,
                  @Assisted("email") String email, @Assisted("cost") int cost, @Assisted("rating") String rating,
                  @Assisted("picture") int picture, @Assisted("capacity") int capacity);
}
