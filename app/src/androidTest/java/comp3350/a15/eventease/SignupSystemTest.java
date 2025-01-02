package comp3350.a15.eventease;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

import android.widget.AdapterView;

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

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.a15.eventease.presentation.LoginActivity;
import comp3350.a15.eventease.presentation.PlannerMainActivity;
import comp3350.a15.eventease.presentation.VendorMainActivity;

@RunWith(AndroidJUnit4.class)
public class SignupSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    private static final SystemTestUtils testUtils = new SystemTestUtils();

    @Before
    public void Setup() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Click the "Allow" button on the notification permission dialog
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
    public void testSignupAsPlanner() {
        Espresso.onView(ViewMatchers.withId(R.id.createNewUserBtn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.eventPlanner)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("fofa"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextSignUpUsername)).perform(ViewActions.typeText("fofa"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextSignUpPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextRepeatPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnCreateUser)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.planner_first_name)).perform(ViewActions.typeText("fofa"));

        Espresso.onView(ViewMatchers.withId(R.id.planner_last_name)).perform(ViewActions.typeText("farooha"));
        Espresso.onView(ViewMatchers.withId(R.id.create_planner_phone)).perform(ViewActions.typeText("87838389"));
        Espresso.onView(ViewMatchers.withId(R.id.create_planner_email)).perform(ViewActions.typeText("hihello@gmail.com"));
        Espresso.onView(ViewMatchers.withId(R.id.create_planner_bio)).perform(ViewActions.typeText("im so tired lol"));
        Espresso.onView(ViewMatchers.withId(R.id.btnCreateUserPlanner)).perform(ViewActions.click());

        // Assert that it navigated to the main page
        Intents.intended(IntentMatchers.hasComponent(PlannerMainActivity.class.getName()));

        //logout as a planner
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());

        //login with the new account credentials
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("fofa"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());
    }


    @Test
    public void testSignupAsVendor() {
        Espresso.onView(ViewMatchers.withId(R.id.createNewUserBtn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.vendor)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("fofaVendor6"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextSignUpUsername)).perform(ViewActions.typeText("fofaVendor6"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextSignUpPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextRepeatPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnCreateUser)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.spinnerServiceType)).perform(ViewActions.click());

        // Select "Decor" option
        Espresso.onData(CoreMatchers.anything()).inAdapterView(isAssignableFrom(AdapterView.class)).atPosition(4).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("fofaVendor6"));

        Espresso.onView(ViewMatchers.withId(R.id.editTextDescription)).perform(ViewActions.typeText("great description"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPhoneNumber)).perform(ViewActions.typeText("87838389"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("hihello@gmail.com"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextCost)).perform(ViewActions.typeText("8700"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextCapacity)).perform(ViewActions.typeText("87"));
        Espresso.onView(ViewMatchers.withId(R.id.btnCreateVendor)).perform(ViewActions.click());

        // Assert that it navigated to the main page
        Intents.intended(IntentMatchers.hasComponent(VendorMainActivity.class.getName()));

        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername)).perform(ViewActions.typeText("fofaVendor6"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("FofaFofa@1"));
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click());

    }


    @After
    public void cleanUp() {
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.logoutBtn)).perform(ViewActions.click());
    }

    @AfterClass
    public static void userCleanup() {
        testUtils.deleteUser("fofa");
        testUtils.deleteUser("fofaVendor6");
    }

}
