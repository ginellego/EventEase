package comp3350.a15.eventease;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
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
import comp3350.a15.eventease.presentation.PlannerMainActivity;
import comp3350.a15.eventease.presentation.VendorMainActivity;

public class ProfileSystemTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

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
    public void TestPlannerProfile() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("planner"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("Planner@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());
        // Assert that it navigated to the planner main page
        Intents.intended(IntentMatchers.hasComponent(PlannerMainActivity.class.getName()));

        //click on profile button
        Espresso.onView(ViewMatchers.withId(R.id.planner_profile_button)).perform(ViewActions.click());
        //assert that i get AwesomePlanner
        Espresso.onView(withId(R.id.plannerName)).check(matches(withText("Awesome Planner")));
        Espresso.onView(withId(R.id.plannerEmail)).check(matches(withText("planner@example.com")));
        Espresso.onView(withId(R.id.plannerPhone)).check(matches(withText("1234567890")));

        //go back backButton
        pressBack();
    }

    @Test
    public void TestVendorProfile() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("vendor"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("Vendor@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());
        // Assert that it navigated to the vendor main page
        Intents.intended(IntentMatchers.hasComponent(VendorMainActivity.class.getName()));

        Espresso.onView(ViewMatchers.withId(R.id.vendor_profile_button)).perform(ViewActions.click());
        //assert that i get vendor profile
        Espresso.onView(withId(R.id.vendorName)).check(matches(withText("Patissier")));
        Espresso.onView(withId(R.id.vendorServiceType)).check(matches(withText("Cake")));
        Espresso.onView(withId(R.id.vendorEmail)).check(matches(withText("vendor1@example.com")));
        Espresso.onView(withId(R.id.vendorDescription)).check(matches(withText("We make cakes !")));
        pressBack();

    }

    @After
    public void cleanUp() {
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
    }
}
