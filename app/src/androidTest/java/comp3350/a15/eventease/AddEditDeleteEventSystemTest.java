package comp3350.a15.eventease;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

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


public class AddEditDeleteEventSystemTest {
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

    @Test
    public void testAddEvent() {
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


        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

        Espresso.onView(withId(R.id.event_title)).check(matches(withText("birthday yay!")));
        Espresso.onView(withId(R.id.event_description)).check(matches(withText("Someones Birthday")));
        Espresso.onView(withId(R.id.event_location)).check(matches(withText("Some place")));

        //then delete it
        Espresso.onView(withId(R.id.close_display_event)).perform(click());
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
    }


    @Test
    public void testEditEventName() {
        //create a new event first
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("party yay!"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones party"));
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

        //then edit it
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.edit_event)));

        Espresso.onView(withId(R.id.new_event_name)).perform(click());
        onView(withId(R.id.new_event_name)).perform(clearText());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("its a wedding now"));
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());
        //Assert
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        Espresso.onView(withId(R.id.event_title)).check(matches(withText("its a wedding now")));

        //then delete it
        Espresso.onView(withId(R.id.close_display_event)).perform(click());
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));

    }


    @Test
    public void testEditEventDescription() {
        //create a new event first
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("another cool event"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("Someones event"));
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

        //then edit it
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.edit_event)));

        Espresso.onView(withId(R.id.new_event_description)).perform(click());
        onView(withId(R.id.new_event_description)).perform(clearText());
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("cool meeting"));
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());
        //Assert
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        Espresso.onView(withId(R.id.event_description)).check(matches(withText("cool meeting")));

        //then delete it
        Espresso.onView(withId(R.id.close_display_event)).perform(click());
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
    }

    @Test
    public void testEditEventLocation() {
        //create a new event first
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("fofa's event"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("my event"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("my place"));
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

        //then edit it
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.edit_event)));

        Espresso.onView(withId(R.id.new_event_location)).perform(click());
        onView(withId(R.id.new_event_location)).perform(clearText());
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("habiba's place"));
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());
        //Assert
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        Espresso.onView(withId(R.id.event_location)).check(matches(withText("habiba's place")));

        //then delete it
        Espresso.onView(withId(R.id.close_display_event)).perform(click());
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));
    }

    @Test
    public void testEditEventDate() {
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("umis event"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("her event"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("her place"));
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

        //then edit it
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.edit_event)));
        Espresso.onView(withId(R.id.new_event_date)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2025, 6, 21));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(withId(R.id.create_button)).perform(click());
        //Assert
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        Espresso.onView(withId(R.id.event_date)).check(matches(withText("2025-06-21")));

        //then delete it
        Espresso.onView(withId(R.id.close_display_event)).perform(click());
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));

    }

    @Test
    public void testEditEventTime() {
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("tomas event"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("tomas event"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("her place"));
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

        //then edit it
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.edit_event)));
        Espresso.onView(withId(R.id.new_event_time)).perform(click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(12, 20));
        Espresso.onView(ViewMatchers.withText("OK")).perform(click());
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.create_button)).perform(click());
        //Assert
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        Espresso.onView(withId(R.id.event_time)).check(matches(withText("12:20")));

        //then delete it
        Espresso.onView(withId(R.id.close_display_event)).perform(click());
        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));


    }

    @Test
    public void testDeleteEvent() {
        Espresso.onView(withId(R.id.addEventBtn)).perform(click());
        Espresso.onView(withId(R.id.new_event_name)).perform(ViewActions.typeText("kamilas event"));
        Espresso.onView(withId(R.id.new_event_description)).perform(ViewActions.typeText("kamilas event"));
        Espresso.onView(withId(R.id.new_event_location)).perform(ViewActions.typeText("her place"));
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


        Espresso.onView(allOf(withId(R.id.events_and_services_recView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(5, SystemTestUtils.clickChildViewWithId(R.id.delete_event)));

        //Assert
        SystemTestUtils testUtils = new SystemTestUtils();
        assertEquals(5, testUtils.getNumOfEvents());
    }

    @After
    public void cleanUp() {
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
    }
}
