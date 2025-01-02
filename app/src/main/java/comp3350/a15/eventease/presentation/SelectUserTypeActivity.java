package comp3350.a15.eventease.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import comp3350.a15.eventease.R;

public class SelectUserTypeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
    }

    public void onEventPlannerBtnClick(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        intent.putExtra("isVendor", false); // Pass false for event planner
        startActivity(intent);
    }

    public void onVendorBtnClick(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        intent.putExtra("isVendor", true); // Pass true for vendor
        startActivity(intent);
    }
}
