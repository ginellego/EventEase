package comp3350.a15.eventease.presentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IPlannerManager;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.objects.factory.IPlannerFactory;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreatePlannerAccountActivity extends AppCompatActivity {
    // SharedPreferences keys
    private static final String USER_PREFS = "userPreferences";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";
    private static final String USER_LOGIN_KEY = "userLogin";
    private static final String IS_VENDOR_KEY = "isVendor";
    @Inject
    IUserManager userManager;
    @Inject
    IPlannerManager plannerManager;
    @Inject
    IPlannerFactory plannerFactory;
    private EditText createPlannerFirstName;
    private EditText createPlannerLastName;
    private EditText createPlannerPhone;
    private EditText createPlannerEmail;
    private EditText createPlannerBio;
    private boolean isVendor;
    private SharedPreferences sharedPreferences;
    private ParcelableUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_planner_account);

        user = getIntent().getParcelableExtra("user", ParcelableUser.class);
        if (user != null) {
            isVendor = user.isVendor();
        }

        createPlannerFirstName = findViewById(R.id.planner_first_name);
        createPlannerLastName = findViewById(R.id.planner_last_name);
        createPlannerPhone = findViewById(R.id.create_planner_phone);
        createPlannerEmail = findViewById(R.id.create_planner_email);
        createPlannerBio = findViewById(R.id.create_planner_bio);

        sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
    }

    public void onCreatePlannerBtnClick(View view) {
        String firstname = createPlannerFirstName.getText().toString().trim();
        String lastname = createPlannerLastName.getText().toString().trim();
        String phone = createPlannerPhone.getText().toString().trim();
        String email = createPlannerEmail.getText().toString().trim();
        String bio = createPlannerBio.getText().toString().trim();

        if (fieldsMissing(firstname, lastname, phone, email, bio)) return;

        try {
            //the password repeat validation is already done in createAccountActivity so at this point
            //the users login credentials should be valid to store in db.
            userManager.registerUser(user.getName(), user.getUsername(), user.getPassword(), user.getPassword(), user.isVendor());

            String username = user.getUsername();
            int accountId = userManager.getUserId(username);
            plannerManager.addPlanner(accountId, firstname, lastname, phone, email, 4, bio);

            // Shared preferences can be considered logic, but they are used here only to store the current state of the user
            storeLoggedInUserCredentials(username);

            Toast.makeText(this, "Planner profile created successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PlannerMainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            userManager.deleteUser(user.getUsername());
            Toast.makeText(this, "There was an issue creating your account. Please try again later...",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean fieldsMissing(String firstname, String lastname, String phone, String email, String bio) {
        if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty() || bio.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void storeLoggedInUserCredentials(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN_KEY, true);
        editor.putString(USER_LOGIN_KEY, username);
        editor.putBoolean(IS_VENDOR_KEY, isVendor);
        editor.apply();
    }
}
