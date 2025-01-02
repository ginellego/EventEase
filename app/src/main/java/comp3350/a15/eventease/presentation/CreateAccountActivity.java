package comp3350.a15.eventease.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.objects.User;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAccountActivity extends AppCompatActivity {
    @Inject
    IUserManager accountManager;
    private EditText editTextName;
    private EditText editTextSignUpUsername;
    private EditText editTextSignUpPassword;
    private EditText editTextRepeatPassword;
    private boolean isVendor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        editTextName = findViewById(R.id.editTextName);
        editTextSignUpUsername = findViewById(R.id.editTextSignUpUsername);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);

        isVendor = getIntent().getBooleanExtra("isVendor", false);
    }

    public void onCreateBtnClick(View view) {
        String name = editTextName.getText().toString();
        String username = editTextSignUpUsername.getText().toString();
        String password = editTextSignUpPassword.getText().toString();
        String passwordRepeat = editTextRepeatPassword.getText().toString();

        if (name.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || passwordRepeat.trim().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            User newUser = accountManager.createUser(name, username, password, passwordRepeat, isVendor);

            Intent intent;
            if (isVendor) {
                intent = new Intent(this, CreateVendorActivity.class);
            } else {
                intent = new Intent(this, CreatePlannerAccountActivity.class);
            }
            ParcelableUser userParcel = new ParcelableUser(newUser);
            intent.putExtra("user", userParcel);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
