package comp3350.a15.eventease;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comp3350.a15.eventease.presentation.LoginActivity;
import comp3350.a15.eventease.presentation.VendorMainActivity;

public class ManageServiceOffersSystemTest {

    @Rule
    public final ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void Setup() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // click the "Allow" button on the notification permission dialog
        try {
            UiObject allowButton = uiDevice.findObject(new UiSelector().text("Allow"));
            if (allowButton.exists() && allowButton.isEnabled()) {
                allowButton.click();
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        Intents.init();

        Espresso.onView(withId(R.id.editTextUsername)).perform(ViewActions.typeText("vendor"));
        Espresso.onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText("Vendor@1"));
        Espresso.onView(withId(R.id.btnLogin)).perform(click());
        // Assert that it navigated to the vendor main page
        Intents.intended(IntentMatchers.hasComponent(VendorMainActivity.class.getName()));
    }

    @Test
    public void testServiceRequestsAdd() {
        //click on requests_button
        Espresso.onView(withId(R.id.requests_button)).perform(click());
        //click on one of the requestsRecView, with id service_request_item at position 0
        Espresso.onView(allOf(withId(R.id.requestsRecView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //click on accept button
        Espresso.onView(withId(R.id.btn_accept_request)).perform(click());
        //go back
        pressBack();
        //will find a card in rec view with id events_and_services_recView, with id id service_request_item at position 0
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //should match text to "Service Type 5"
        Espresso.onView(withId(R.id.service_type)).check(matches(withText("Service Type 5")));
        pressBack();
    }

    @Test
    public void testServiceRequestsDecline() {
        //click on requests_button
        Espresso.onView(withId(R.id.requests_button)).perform(click());
        //click on one of the requestsRecView, with id service_request_item at position 0
        Espresso.onView(allOf(withId(R.id.requestsRecView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //click on accept button
        Espresso.onView(withId(R.id.btn_reject_request)).perform(click());
        Espresso.onView(withText("Are you sure you want to decline this request?"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        Espresso.onView(withText("Yes"))
                .inRoot(isDialog())
                .perform(click());
        //go back
        pressBack();
        Espresso.onView(withId(R.id.rejectedRequests)).perform(click());
        //will find a card in rec view with id events_and_services_recView, with id id service_request_item at position 0
        Espresso.onView(allOf(withId(R.id.r_requestsRecView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //should match text to "Service Type 1"
        Espresso.onView(withId(R.id.service_type)).check(matches(withText("Service Type 2")));
        pressBack();
        pressBack();

    }

    @After
    public void cleanUp() {
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
    }
}
