package comp3350.a15.eventease.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.ExperimentalBadgeUtils;

import java.util.List;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.application.qualifier.AcceptedServiceRequest;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.presentation.adapter.AcceptedRequestsRecViewAdapter;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IAcceptedRequestsRecView;
import comp3350.a15.eventease.presentation.enums.RequestStatusCode;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorMainActivity extends AppCompatActivity implements IAcceptedRequestsRecView {
    // SharedPreferences keys
    private static final String USER_PREFS = "userPreferences";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";
    private static final String USER_LOGIN_KEY = "userLogin";
    private static final String NEW_REQUESTS = "new_requests";
    private static final String NUM_REQUESTS = "num_requests";
    public static final String VENDOR_ID_KEY = "vendor_id";
    @Inject
    @AcceptedServiceRequest
    IServiceRequestManager acceptedRequestManager;
    @Inject
    @UnresolvedServiceRequest
    IServiceRequestManager unresolvedRequestManager;
    @Inject
    IUserManager userManager;
    @Inject
    NotificationUtils notificationUtils;
    @Inject
    IVendorManager vendorManager;

    List<ServiceRequest> listOfAcceptedRequests;

    private TextView noMoreServices;
    private View newRequestsBtn;
    private BadgeDrawable numOfRequestsBadge;
    private FrameLayout requestBtnFrameLayout;
    private AcceptedRequestsRecViewAdapter acceptedRequestsAdapter;
    private SharedPreferences sharedPreferences;
    private int userID;
    @SuppressLint("NotifyDataSetChanged")
    private final ActivityResultLauncher<Intent> viewUnresolvedRequestActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RequestStatusCode.ACCEPTED.getStatusCode()) {
                    listOfAcceptedRequests = acceptedRequestManager.getAllRequests(userID);
                    acceptedRequestsAdapter.setRequestsList(listOfAcceptedRequests);
                    acceptedRequestsAdapter.notifyDataSetChanged();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        verifyUserLogin();

        String username = sharedPreferences.getString(USER_LOGIN_KEY, "Vendor");

        RecyclerView servicesRecView = findViewById(R.id.events_and_services_recView);
        noMoreServices = findViewById(R.id.no_more_events_or_services);
        TextView welcomeText = findViewById(R.id.welcomeText);
        String welcomeMessage = getString(R.string.welcome_vendor_string, username); // Dynamically change the string since it has a placeholder
        welcomeText.setText(welcomeMessage);

        handleNewRequestNotificationClick();

        TextView myServices = findViewById(R.id.my_events_or_services);
        newRequestsBtn = findViewById(R.id.requests_button);
        requestBtnFrameLayout = findViewById(R.id.btn_request_frame_layout);

        userID = userManager.getUserId(username);
        listOfAcceptedRequests = acceptedRequestManager.getAllRequests(userID);
        acceptedRequestsAdapter = new AcceptedRequestsRecViewAdapter(listOfAcceptedRequests, this);

        servicesRecView.setAdapter(acceptedRequestsAdapter);
        servicesRecView.setLayoutManager(new LinearLayoutManager(this));

        myServices.setText(R.string.static_my_services_string);
        noMoreServices.setText(R.string.no_scheduled_services);

        createNotificationForNewRequests();
    }

    private void handleNewRequestNotificationClick() {
        // Check if the activity was launched by a notification click
        Intent intent = getIntent();
        if (intent.hasExtra("userId")) {
            int id = intent.getIntExtra("userId", -1);

            Intent newIntent = new Intent(this, UnresolvedServiceRequestsActivity.class);
            newIntent.putExtra("userId", id);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(newIntent);
        }
    }

    // Logic? Yes
    // Can it be done in any other layer in the app? No
    // Is it crucial for app to work? Yes
    private void verifyUserLogin() {
        // Throw user out of this activity and send him to Login page if they are not logged in
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false);
        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listOfAcceptedRequests = acceptedRequestManager.getAllRequests(userID);
        setUpRequestsCounterBadge();
        showOrHideNoMoreRequestsText();
    }

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    private void setUpRequestsCounterBadge() {
        int numberOfNewRequests = unresolvedRequestManager.getAllRequests(userID).stream()
                .filter(request -> request.getServiceStatus() == ServiceRequest.ServiceStatus.NEW)
                .toArray().length;

        if (numberOfNewRequests != 0) {
            numOfRequestsBadge = BadgeDrawable.create(this);
            requestBtnFrameLayout.setForeground(numOfRequestsBadge);
            numOfRequestsBadge.setMaxNumber(99);

            numOfRequestsBadge.setNumber(numberOfNewRequests);

            requestBtnFrameLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom)
                    -> numOfRequestsBadge.updateBadgeCoordinates(newRequestsBtn, requestBtnFrameLayout));
        } else {
            if (numOfRequestsBadge != null) {
                requestBtnFrameLayout.setForeground(null);
            }
        }
    }

    private void showOrHideNoMoreRequestsText() {
        if (acceptedRequestsAdapter.getItemCount() == 0) {
            showEmptyText();
        } else {
            hideEmptyText();
        }
    }

    private void showEmptyText() {
        noMoreServices.setVisibility(View.VISIBLE);
    }

    private void hideEmptyText() {
        noMoreServices.setVisibility(View.GONE);
    }

    // Logic? Yes
    // Can it be done in any other layer in the app? No
    // Is it crucial for app to work? Yes
    public void logout(View view) {
        // clear Shared Preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(IS_LOGGED_IN_KEY);
        editor.remove(USER_LOGIN_KEY);
        editor.apply();

        // verifyUserLogin() will throw user to Login Page
        verifyUserLogin();
    }

    public void onNewRequestsBtnClick(View v) {
        Intent intent = new Intent(this, UnresolvedServiceRequestsActivity.class);
        intent.putExtra("userId", userID);
        viewUnresolvedRequestActivityLauncher.launch(intent);
    }

    public void onRejectedRequestsBtnClick(View v) {
        Intent intent = new Intent(this, RejectedServiceRequestsActivity.class);
        intent.putExtra("userId", userID);
        viewUnresolvedRequestActivityLauncher.launch(intent);
    }

    public void onProfileBtnClick(View v) {
        Intent intent = new Intent(this, VendorProfileActivity.class);
        Vendor currVendor = vendorManager.getVendorByAccountId(userID);
        intent.putExtra("vendor", new ParcelableVendor(currVendor));
        startActivity(intent);
    }

    @Override
    public void onRequestCardClick(int position) {
        Intent intent = new Intent(this, ServiceRequestDetailsActivity.class);
        intent.putExtra("request", new ParcelableServiceRequest(
                listOfAcceptedRequests.get(position)));
        startActivity(intent);
    }

    private void createNotificationForNewRequests() {
        // Check SharedPreferences for pending broadcasts
        SharedPreferences broadcastManagerSharedPreferences = getSharedPreferences(NEW_REQUESTS, MODE_PRIVATE);

        String numRequestsKey = NUM_REQUESTS + "_" + userID;
        String vendorKey = VENDOR_ID_KEY + "_" + userID;
        int numNewRequests = broadcastManagerSharedPreferences.getInt(numRequestsKey, 0);
        int vendorId = broadcastManagerSharedPreferences.getInt(vendorKey, -1);

        if (userID == vendorId && numNewRequests > 0) {
            // Create a notification for new requests which opens the list of pending requests on click
            notificationUtils.showNewRequestNotification(this, numNewRequests, userID);

            // Remove the entry from SharedPreferences
            broadcastManagerSharedPreferences.edit().remove(numRequestsKey).apply();
            broadcastManagerSharedPreferences.edit().remove(vendorKey).apply();
        }
    }
}
