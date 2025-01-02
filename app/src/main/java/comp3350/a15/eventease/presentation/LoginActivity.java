package comp3350.a15.eventease.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IUserManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    // SharedPreferences keys
    private static final String USER_PREFS = "userPreferences";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";
    private static final String USER_LOGIN_KEY = "userLogin";
    private static final String IS_VENDOR_KEY = "isVendor";
    @Inject
    IUserManager userManager;

    private EditText loginField;
    private EditText passwordField;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Check if the permission is not granted
        boolean hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST_CODE
            );
        }
        loginField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        verifyLoginStatus();
    }

    private void verifyLoginStatus() {
        // Logic? Kind of
        // Can it possibly be done in any other layer? No
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false);
        if (isLoggedIn) {
            redirectToCorrectActivity();
        }
    }

    private void redirectToCorrectActivity() {
        // Logic? Kind of
        // Can it possibly be done in any other layer? No
        try {
            String username = sharedPreferences.getString(USER_LOGIN_KEY, "");
            boolean isVendor = userManager.isUserVendor(username);
            Intent intent;
            if (!isVendor) {
                intent = new Intent(this, PlannerMainActivity.class);
            } else {
                intent = new Intent(this, VendorMainActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "There was an error logging in", Toast.LENGTH_LONG).show(); // for user
            System.out.println(e.getMessage()); // for developer
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Notification permission denied. Please enable it in the app settings to receive notifications.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onLoginBtnClick(View view) {
        String login = loginField.getText().toString();
        String password = passwordField.getText().toString();

        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, "Please enter both login and password", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // loginUser is a void function. It either does nothing (on success) or throws an error with explanation of why it errored
            userManager.loginUser(login, password);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGGED_IN_KEY, true);
            editor.putString(USER_LOGIN_KEY, login);
            // userManager checks whether the user is vendor or planner
            editor.putBoolean(IS_VENDOR_KEY, userManager.isUserVendor(login));
            editor.apply();

            redirectToCorrectActivity();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onNewUserBtnClick(View view) {
        Intent intent = new Intent(this, SelectUserTypeActivity.class);
        startActivity(intent);
    }
}
