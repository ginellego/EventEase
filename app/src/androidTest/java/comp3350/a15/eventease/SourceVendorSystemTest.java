package comp3350.a15.eventease;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.os.SystemClock;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
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

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comp3350.a15.eventease.presentation.LoginActivity;
import comp3350.a15.eventease.presentation.PlannerMainActivity;
import comp3350.a15.eventease.presentation.VendorMainActivity;

public class SourceVendorSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    private static final SystemTestUtils testUtils = new SystemTestUtils();


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


    }

    @Test
    public void testVendorSourcing() {
        onView(ViewMatchers.withId(R.id.createNewUserBtn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.vendor)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("fofaVendor"));
        onView(ViewMatchers.withId(R.id.editTextSignUpUsername)).perform(ViewActions.typeText("fofaVendor"));
        onView(ViewMatchers.withId(R.id.editTextSignUpPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        onView(ViewMatchers.withId(R.id.editTextRepeatPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        onView(ViewMatchers.withId(R.id.btnCreateUser)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.spinnerServiceType)).perform(ViewActions.click());


        onData(CoreMatchers.anything()).inAdapterView(isAssignableFrom(AdapterView.class)).atPosition(4).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("fofaVendor"));

        onView(ViewMatchers.withId(R.id.editTextDescription)).perform(ViewActions.typeText("great description"));
        onView(ViewMatchers.withId(R.id.editTextPhoneNumber)).perform(ViewActions.typeText("87838389"));
        onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("hihello@gmail.com"));
        onView(ViewMatchers.withId(R.id.editTextCost)).perform(ViewActions.typeText("8700"));
        onView(ViewMatchers.withId(R.id.editTextCapacity)).perform(ViewActions.typeText("87"));
        onView(ViewMatchers.withId(R.id.btnCreateVendor)).perform(ViewActions.click());

        // Assert that it navigated to the main page
        Intents.intended(IntentMatchers.hasComponent(VendorMainActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("planner"));
        onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("Planner@1"));
        onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());
        // Assert that it navigated to the planner main page
        Intents.intended(IntentMatchers.hasComponent(PlannerMainActivity.class.getName()));

        //create new event
        onView(withId(R.id.addEventBtn)).perform(click());
        onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("birthday yay!"));
        onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones Birthday"));
        onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("Some place"));
        onView(withId(R.id.new_event_date)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 5, 21));
        onView(ViewMatchers.withText("OK")).perform(click());
        onView(withId(R.id.new_event_time)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        onView(ViewMatchers.withText("OK")).perform(click());
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create_button)).perform(click());

        //click on browse vendors
        onView(withId(R.id.browseVendors)).perform(click());
        onView(allOf(withId(R.id.vendorRecView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, SystemTestUtils.clickChildViewWithId(R.id.eventDropdown)));

        onData(CoreMatchers.anything()).inAdapterView(isAssignableFrom(AdapterView.class)).atPosition(6).perform(ViewActions.click());

        onView(allOf(withId(R.id.vendorRecView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, SystemTestUtils.clickChildViewWithId(R.id.saveVendor_button)));

        pressBack();
        onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

        //assert that the vendor was added

        // Get the text of the TextView in the RecyclerView item at position 0
        onView(allOf(withId(R.id.vendorName), isDescendantOfA(withId(R.id.requestsRecViewUnderEvent)))).check(matches(withText("fofaVendor")));
        pressBack();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("fofaVendor"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());

        //Assert that request is there
        //click on the first
        Espresso.onView(withId(R.id.requests_button)).perform(click());
        Espresso.onView(allOf(withId(R.id.requestsRecView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.onView(ViewMatchers.withId(R.id.request_event_name)).check(matches(withText("birthday yay!")));
        pressBack();
        pressBack();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("planner"));
        onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("Planner@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());

        SystemClock.sleep(1000);
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
        SystemClock.sleep(1000);
    }


    @Test
    public void testVendorsourcingSecondWay() {
        onView(ViewMatchers.withId(R.id.createNewUserBtn)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.vendor)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("fofaVendor2"));
        onView(ViewMatchers.withId(R.id.editTextSignUpUsername)).perform(ViewActions.typeText("fofaVendor2"));
        onView(ViewMatchers.withId(R.id.editTextSignUpPassword)).perform(ViewActions.typeText("FofaFofa@2"));
        onView(ViewMatchers.withId(R.id.editTextRepeatPassword)).perform(ViewActions.typeText("FofaFofa@2"));
        onView(ViewMatchers.withId(R.id.btnCreateUser)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.spinnerServiceType)).perform(ViewActions.click());

        // Select "Decor" option
        onData(CoreMatchers.anything()).inAdapterView(isAssignableFrom(AdapterView.class)).atPosition(3).perform(ViewActions.click());


        onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("fofaVendor2"));

        onView(ViewMatchers.withId(R.id.editTextDescription)).perform(ViewActions.typeText("great description"));
        onView(ViewMatchers.withId(R.id.editTextPhoneNumber)).perform(ViewActions.typeText("87838389"));
        onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("hihello@gmail.com"));
        onView(ViewMatchers.withId(R.id.editTextCost)).perform(ViewActions.typeText("8700"));
        onView(ViewMatchers.withId(R.id.editTextCapacity)).perform(ViewActions.typeText("87"));
        onView(ViewMatchers.withId(R.id.btnCreateVendor)).perform(ViewActions.click());


        // Assert that it navigated to the main page
        Intents.intended(IntentMatchers.hasComponent(VendorMainActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());


        onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("planner"));
        onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("Planner@1"));
        onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());
        // Assert that it navigated to the planner main page
        Intents.intended(IntentMatchers.hasComponent(PlannerMainActivity.class.getName()));


        //create new event
        onView(withId(R.id.addEventBtn)).perform(click());
        onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("wedding yay!"));
        onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones wedding"));
        onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("Some place"));
        onView(withId(R.id.new_event_date)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 5, 21));
        onView(ViewMatchers.withText("OK")).perform(click());
        onView(withId(R.id.new_event_time)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        onView(ViewMatchers.withText("OK")).perform(click());
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create_button)).perform(click());

        onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

        //click on add vendors button addVendorToEvent
        onView(withId(R.id.addVendorToEvent)).perform(click());
        //choose a category choose that u chose above(music)
        onView(withId(R.id.music)).perform(click());
        //click get vendors button
        onView(withId(R.id.get_vendors)).perform(click());
        //click on the add button saveVendor_button
        onView(withId(R.id.saveVendor_button)).perform(click());
        //go back
        pressBack();
        //go back
        pressBack();
        //go back
        pressBack();
        //click on the 6th card
        onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        //assert that the vendor u added is there
        onView(allOf(withId(R.id.vendorName), isDescendantOfA(withId(R.id.requestsRecViewUnderEvent)))).check(matches(withText("fofaVendor2")));

        //click on close close_display_event
        onView(withId(R.id.close_display_event)).perform(click());
        //clean up

        SystemClock.sleep(1000);
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
        SystemClock.sleep(1000);
    }

    @After
    public void cleanUp() {
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
    }

    @AfterClass
    public static void userCleanup() {
        testUtils.deleteUser("fofaVendor");
        testUtils.deleteUser("fofaVendor2");
    }
}
