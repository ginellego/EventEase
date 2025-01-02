package comp3350.a15.eventease;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.a15.eventease.logic.BaseServiceRequestManagerIT;
import comp3350.a15.eventease.logic.EventManagerIT;
import comp3350.a15.eventease.logic.RequestInvoiceManagerIT;
import comp3350.a15.eventease.logic.UserManagerIT;
import comp3350.a15.eventease.logic.VendorManagerIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({BaseServiceRequestManagerIT.class, EventManagerIT.class,UserManagerIT.class, RequestInvoiceManagerIT.class,VendorManagerIT.class})
public class IntegrationTests {

}