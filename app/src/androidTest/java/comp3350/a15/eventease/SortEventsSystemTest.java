package comp3350.a15.eventease;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.os.SystemClock;
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

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comp3350.a15.eventease.presentation.LoginActivity;
import comp3350.a15.eventease.presentation.PlannerMainActivity;

public class SortEventsSystemTest {

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

        Espresso.onView(withId(R.id.editTextUsername)).perform(ViewActions.typeText("planner"));
        Espresso.onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText("Planner@1"));
        Espresso.onView(withId(R.id.btnLogin)).perform(click());
        // Assert that it navigated to the planner main page
        Intents.intended(IntentMatchers.hasComponent(PlannerMainActivity.class.getName()));

    }

    //sort by name
    @Test
    public void testSortByName() {
        //add two new events with a and b to check if names are sorted

        //EVENT 1
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("birthday yay!"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones Birthday"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("Some place"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 5, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());


        //EVENT2
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 5, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());

        //click on sort
        Espresso.onView(withId(R.id.sortEventsButton)).perform(click());
        Espresso.onView(withText("Name")).perform(click());

        //assert that it changed
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("apple")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("birthday yay!")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        //delete both
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));


    }

    //sort by eventdate
    @Test
    public void testSortByEventDate() {

        //EVENT 1
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("birthday yay!"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones Birthday"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("Some place"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2030, 5, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());


        //EVENT2
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2025, 1, 1));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());

        //click on sort
        Espresso.onView(withId(R.id.sortEventsButton)).perform(click());
        Espresso.onView(withText("Event Date")).perform(click());

        //assert that it changed
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("apple")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("birthday yay!")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        //delete both
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));


    }

    //sort by date added old -> new
    @Test
    public void testSortByDateAddedOld() {
        //add events after each other
        //wait a bit in between
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("boo!"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones Birthday"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("Some place"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 9, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());

        SystemClock.sleep(5000);

        //EVENT2
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("apples"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 9, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());

        //click on sort
        Espresso.onView(withId(R.id.sortEventsButton)).perform(click());
        Espresso.onView(withText("Date Added(old -> new)")).perform(click());

        //assert that it changed
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("boo!")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("apples")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        //delete both
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));


    }

    //sort by date added new -> old
    @Test
    public void testSortByDateAddedNew() {
        //add events after each other
        //wait a bit in between
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("cat"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones Birthday"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("Some place"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 9, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());

        SystemClock.sleep(5000);

        //EVENT2
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("best"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("apple"));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 9, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(15, 30));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());

        //click on sort
        Espresso.onView(withId(R.id.sortEventsButton)).perform(click());
        Espresso.onView(withText("Date Added(new -> old)")).perform(click());

        //assert that it changed
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("best")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("cat")));
        Espresso.onView(withId(R.id.close_display_event)).perform(click());

        //delete both
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));


    }

    @After
    public void cleanUp() {
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
    }
}
