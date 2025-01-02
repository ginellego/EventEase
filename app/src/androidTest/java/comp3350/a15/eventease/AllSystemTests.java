package comp3350.a15.eventease;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
        SignupSystemTest.class,
        LogInSystemTest.class,
        LogoutSystemTest.class,
        AddEditDeleteEventSystemTest.class,
        SortEventsSystemTest.class,
        ManageServiceOffersSystemTest.class,
        RevisitDeclinedRequestsSystemTest.class,
        SourceVendorSystemTest.class,
        ProfileSystemTest.class
})
@RunWith(Suite.class)
public class AllSystemTests {

}
