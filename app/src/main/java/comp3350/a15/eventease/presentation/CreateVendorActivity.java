package comp3350.a15.eventease.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.objects.factory.IVendorFactory;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateVendorActivity extends AppCompatActivity {
    // SharedPreferences keys
    private static final String USER_PREFS = "userPreferences";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";
    private static final String USER_LOGIN_KEY = "userLogin";
    private static final String IS_VENDOR_KEY = "isVendor";
    public static final String SERVICE_TYPE_DEFAULT_HINT = "Select a service type";
    @Inject
    protected IVendorManager vendorManager;
    @Inject
    protected IUserManager userManager;
    @Inject
    protected IVendorFactory vendorFactory;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;
    private EditText editTextCost;
    private EditText editTextCapacity;
    private Spinner spinnerServiceType;
    private SharedPreferences sharedPreferences;
    private ParcelableUser user;
    private boolean isVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vendor);

        user = getIntent().getParcelableExtra("user", ParcelableUser.class);
        if (user != null) {
            isVendor = user.isVendor();
        }

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextCost = findViewById(R.id.editTextCost);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        spinnerServiceType = findViewById(R.id.spinnerServiceType);
        String[] serviceTypes = getResources().getStringArray(R.array.service_types);
        spinnerServiceType.setAdapter(getArrayAdapter(serviceTypes));
        spinnerServiceType.setOnItemSelectedListener(getOnItemSelectedListener());

        try {
            sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open page", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @NonNull
    private static AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) view).setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        };
    }

    @NonNull
    private ArrayAdapter<String> getArrayAdapter(String[] serviceTypes) {
        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serviceTypes) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    view.setTextColor(Color.GRAY);
                }
                return view;
            }
        };
    }

    public void onCreateVendorBtnClick(View view) {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String cost = editTextCost.getText().toString();
        String capacity = editTextCapacity.getText().toString();
        String serviceType = spinnerServiceType.getSelectedItem().toString();

        if (fieldMissing(name, description, phoneNumber, email, cost, capacity)) return;

        ServiceImageMapper mapper = new ServiceImageMapper(this);
        int image;
        try {
            image = mapper.getImageResource(serviceType);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to create, please try again", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            image = mapper.getImageResource(serviceType);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to create, please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            userManager.registerUser(user.getName(), user.getUsername(), user.getPassword(), user.getPassword(), user.isVendor());

            String username = user.getUsername();
            int accountId = userManager.getUserId(username);
            Vendor newVendor = vendorFactory.create(accountId, name, serviceType, description, phoneNumber, email, Integer.parseInt(cost), "4.3", image, Integer.parseInt(capacity));
            vendorManager.addNewVendor(newVendor);

            // Shared preferences can be considered logic, but they are used here only to store the current state of the user
            storeLoggedInUserCredentials(username);

            Toast.makeText(this, "Vendor created successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, VendorMainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            userManager.deleteUser(user.getUsername());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean fieldMissing(String name, String description, String phoneNumber, String email, String cost, String capacity) {
        boolean aFieldMissing = false;
        if (name.trim().isEmpty() || description.trim().isEmpty() || phoneNumber.trim().isEmpty() ||
                email.trim().isEmpty() || cost.trim().isEmpty() || capacity.trim().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            aFieldMissing = true;
        }
        if (spinnerServiceType.getSelectedItem().equals(SERVICE_TYPE_DEFAULT_HINT)) {
            Toast.makeText(this, "Please select a service type", Toast.LENGTH_SHORT).show();
            aFieldMissing = true;
        }

        return aFieldMissing;
    }

    private void storeLoggedInUserCredentials(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN_KEY, true);
        editor.putString(USER_LOGIN_KEY, username);
        editor.putBoolean(IS_VENDOR_KEY, isVendor);
        editor.apply();
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private static class ServiceImageMapper {
        private final Properties mappings;
        private final Context context;

        public ServiceImageMapper(Context context) {
            this.context = context;
            mappings = new Properties();
            try {
                mappings.load(context.getAssets().open("service_mappings.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int getImageResource(String serviceType) {
            String imageName = mappings.getProperty(serviceType);
            if (imageName != null) {
                return getResourceIdByName(imageName);
            } else {
                return 0; // Or return a default image resource ID
            }
        }

        @SuppressLint("DiscouragedApi")
        private int getResourceIdByName(String resourceName) {
            return getResources().getIdentifier(resourceName, "drawable", getPackageName());
        }

        private Resources getResources() {
            return context.getResources();
        }

        private String getPackageName() {
            return context.getPackageName();
        }
    }
}


