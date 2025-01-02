package comp3350.a15.eventease.presentation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IPlannerManager;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.logic.exceptions.PlannerNotFoundException;
import comp3350.a15.eventease.objects.Planner;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlannerProfileActivity extends AppCompatActivity {

    @Inject
    IPlannerManager plannerManager;
    @Inject
    IUserManager userManager;
    Planner planner;


    private static final String USER_PREFS = "userPreferences";
    private static final String USER_LOGIN_KEY = "userLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_profile);
        TextView plannerName = findViewById(R.id.plannerName);
        TextView rating = findViewById(R.id.plannerRating);
        TextView plannerPhone = findViewById(R.id.plannerPhone);
        TextView plannerEmail = findViewById(R.id.plannerEmail);
        TextView plannerBio = findViewById(R.id.plannerBio);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String username = sharedPreferences.getString(USER_LOGIN_KEY, "Planner");
        int userID = userManager.getUserId(username);
        if (userID != -1) {
            try {
                planner = plannerManager.getPlannerByUserId(userID);

                if (planner != null) {
                    plannerName.setText(planner.getFullname());
                    rating.setText(String.valueOf(planner.getRating()));
                    plannerPhone.setText(planner.getPhoneNumber());
                    plannerEmail.setText(planner.getEmail());
                    plannerBio.setText(planner.getBio());
                }
            } catch (PlannerNotFoundException e) {
                Toast.makeText(this, "The planner was not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            finish();
        }
    }
}
