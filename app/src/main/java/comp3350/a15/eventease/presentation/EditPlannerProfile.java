package comp3350.a15.eventease.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IPlannerManager;
import comp3350.a15.eventease.objects.Planner;


public class EditPlannerProfile extends AppCompatActivity {
    @Inject
    IPlannerManager plannerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_planner_profile);

        Planner currPlanner = getIntent().getParcelableExtra("planner", ParcelablePlanner.class).toPlanner(plannerManager);

        TextView plannerFirstName = findViewById(R.id.planner_firstname_input);
        TextView plannerLastName = findViewById(R.id.planner_lastname_input);
        EditText plannerPhone = findViewById(R.id.planner_phone_input);
        EditText plannerEmail = findViewById(R.id.planner_email_input);
        EditText plannerBio = findViewById(R.id.planner_bio_input);

        if (currPlanner != null) {
            plannerFirstName.setText(currPlanner.getFirstname());
            plannerLastName.setText(currPlanner.getLastname());
            plannerPhone.setText(currPlanner.getPhoneNumber());
            plannerEmail.setText(currPlanner.getEmail());
            plannerBio.setText(currPlanner.getBio());
        }
    }

    public void goToPlannerProfile(View view) {
        Intent goToPlannerProfile = new Intent(this, PlannerProfileActivity.class);
        startActivity(goToPlannerProfile);
    }
}
