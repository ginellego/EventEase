package comp3350.a15.eventease;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.a15.eventease.presentation.LoginActivity;
import comp3350.a15.eventease.presentation.PlannerMainActivity;
import comp3350.a15.eventease.presentation.VendorMainActivity;

@RunWith(AndroidJUnit4.class)
public class LogInSystemTest {
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
    public void testPlannerLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("planner"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("Planner@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());
        // Assert that it navigated to the planner main page
        Intents.intended(IntentMatchers.hasComponent(PlannerMainActivity.class.getName()));


    }

    @Test
    public void testVendorLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("vendor"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("Vendor@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());
        // Assert that it navigated to the vendor main page
        Intents.intended(IntentMatchers.hasComponent(VendorMainActivity.class.getName()));


    }

    @After
    public void cleanUp() {
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
    }
}
