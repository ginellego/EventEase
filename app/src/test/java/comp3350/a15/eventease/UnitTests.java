package comp3350.a15.eventease;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.a15.eventease.logic.BaseServiceRequestManagerTest;
import comp3350.a15.eventease.logic.EventManagerTest;
import comp3350.a15.eventease.logic.PlannerManagerTest;
import comp3350.a15.eventease.logic.RequestInvoiceManagerTest;
import comp3350.a15.eventease.logic.UserManagerTest;
import comp3350.a15.eventease.logic.VendorManagerTest;
import comp3350.a15.eventease.objects.EventTest;
import comp3350.a15.eventease.objects.InvoiceTest;
import comp3350.a15.eventease.objects.PlannerTest;
import comp3350.a15.eventease.objects.ServiceRequestTest;
import comp3350.a15.eventease.objects.UserTest;
import comp3350.a15.eventease.objects.VendorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({EventTest.class, EventManagerTest.class,
        ServiceRequestTest.class, BaseServiceRequestManagerTest.class,
        UserTest.class, UserManagerTest.class,
        InvoiceTest.class, RequestInvoiceManagerTest.class,
        PlannerTest.class, PlannerManagerTest.class,
        VendorManagerTest.class, VendorTest.class})
public class UnitTests {

}
