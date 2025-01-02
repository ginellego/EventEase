package comp3350.a15.eventease.presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.logic.exceptions.EventNotDeletedException;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.presentation.adapter.EventsRecViewAdapter;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IEventsRecView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlannerMainActivity extends AppCompatActivity implements IEventsRecView {
    public static final String ACCEPTED_REQUESTS = "accepted_requests";
    public static final String REJECTED_REQUESTS = "rejected_requests";    // SharedPreferences keys
    private static final String USER_PREFS = "userPreferences";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";
    private static final String USER_LOGIN_KEY = "userLogin";
    @Inject
    IEventManager eventManager;
    @Inject
    @UnresolvedServiceRequest
    IServiceRequestManager serviceRequestManager;
    @Inject
    IVendorManager vendorManager;
    @Inject
    IUserManager userManager;
    @Inject
    IRequestInvoiceManager invoiceManager;
    @Inject
    NotificationUtils notificationUtils;

    private EventsRecViewAdapter eventsRecViewAdapter;
    private TextView noMoreEvents;
    private int userID;
    private SharedPreferences sharedPreferences;
    private SharedPreferences broadcastManagerSharedPreferences;
    private List<Event> listOfEvents;
    private final ActivityResultLauncher<Intent> modifyPersistenceActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshEvents(-1);
                }
            }
    );

    private static ParcelableServiceRequest deserializeRequest(Object value) {
        Gson gson = new Gson();
        return gson.fromJson(value.toString(), ParcelableServiceRequest.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_main);

        sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        verifyUserLogin();

        String username = sharedPreferences.getString(USER_LOGIN_KEY, "Planner");

        handleAcceptedRequestNotificationClick();
        handleRejectedRequestNotificationClick();

        TextView welcomeText = findViewById(R.id.welcomeText);
        String welcomeMessage = getString(R.string.welcome_planner_string, username); // Dynamically change the string since it has a placeholder
        welcomeText.setText(welcomeMessage);

        RecyclerView eventsRecView = findViewById(R.id.events_and_services_recView);
        noMoreEvents = findViewById(R.id.no_more_events_or_services);
        TextView myEvents = findViewById(R.id.my_events_or_services);
        FloatingActionButton sortButton = findViewById(R.id.sortEventsButton);

        userID = userManager.getUserId(username);
        listOfEvents = eventManager.getAllEvents(userID);
        eventsRecViewAdapter = new EventsRecViewAdapter(listOfEvents, this);

        eventsRecView.setAdapter(eventsRecViewAdapter);
        eventsRecView.setLayoutManager(new LinearLayoutManager(this));

        noMoreEvents.setText(R.string.no_scheduled_events);
        myEvents.setText(R.string.static_my_events_string);
        sortButton.setOnClickListener(view -> showSortDialog());

        showOrHideNoMoreEventsText();

        createNotificationsForRejectedRequests();
        createNotificationsForAcceptedRequests();
    }

    private void handleAcceptedRequestNotificationClick() {
        // Check if the activity was launched by a notification click
        if (getIntent().hasExtra("invoice")) {
            // Extract invoice details from the intent
            ParcelableInvoice invoice = getIntent().getParcelableExtra("invoice", ParcelableInvoice.class);

            // Show the InvoiceDialogFragment
            showInvoiceDialog(invoice);
        }
    }

    private void handleRejectedRequestNotificationClick() {
        // Check if the activity was launched by a notification click
        Intent intent = getIntent();
        if (intent.hasExtra("event")) {
            ParcelableEvent eventParcel = intent.getParcelableExtra("event", ParcelableEvent.class);

            Intent newIntent = new Intent(this, DisplayEventActivity.class);
            newIntent.putExtra("event", eventParcel);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(newIntent);
        }
    }

    private void createNotificationsForAcceptedRequests() {
        // Check SharedPreferences for pending broadcasts
        broadcastManagerSharedPreferences = getSharedPreferences(ACCEPTED_REQUESTS, MODE_PRIVATE);
        Map<String, ?> allEntries = broadcastManagerSharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            Invoice invoice = fetchInvoice(entry.getValue());

            if (invoice != null && invoice.getPlannerId() == userID) {
                // Create a notification using the request information and
                createNotification(Optional.of(invoice), Optional.empty());

                // Remove the entry from SharedPreferences
                broadcastManagerSharedPreferences.edit().remove(key).apply();
            }
        }
    }

    private void createNotificationsForRejectedRequests() {
        // Check SharedPreferences for pending broadcasts
        broadcastManagerSharedPreferences = getSharedPreferences(REJECTED_REQUESTS, MODE_PRIVATE);
        Map<String, ?> allEntries = broadcastManagerSharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            ParcelableServiceRequest request = deserializeRequest(entry.getValue());

            if (request != null && request.getPlannerId() == userID) {
                // Create a notification using the request information and
                createNotification(Optional.empty(), Optional.of(request.toServiceRequest(serviceRequestManager)));

                // Remove the entry from SharedPreferences
                broadcastManagerSharedPreferences.edit().remove(key).apply();
            }
        }
    }

    private Invoice fetchInvoice(Object invoiceId) {
        return invoiceManager.getInvoice((int) invoiceId);
    }

    private void createNotification(Optional<Invoice> invoice, Optional<ServiceRequest> request) {
        invoice.ifPresent(inv -> notificationUtils.showRespondedRequestNotification(this, "Request Accepted", "The request for " +
                inv.getServiceType() + " service was accepted", invoice, Optional.empty(), true));
        request.ifPresent(
                rq -> notificationUtils.showRespondedRequestNotification(this, "Request Declined",
                        "The request for " + rq.getServiceType() + " service was declined", Optional.empty(), request, false));
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

    private void showSortDialog() {
        String[] sortOptions = getResources().getStringArray(R.array.spinner_items);

        AlertDialog.Builder builder = new AlertDialog.Builder(PlannerMainActivity.this);
        builder.setTitle("Sort by")
                .setItems(sortOptions, (dialog, which) -> {
                    String selectedSortOption = sortOptions[which];
                    eventManager.setSortParameter(selectedSortOption);
                    refreshEvents(-1);
                });
        builder.create().show();
    }

    public void launchAddNewEventActivity(View view) {
        Intent addEventIntent = new Intent(this, AddEventActivity.class);
        modifyPersistenceActivityResultLauncher.launch(addEventIntent);
    }

    @Override
    public void onEventCardClick(int position) {
        Intent intent = new Intent(this, DisplayEventActivity.class);
        ParcelableEvent eventParcel = new ParcelableEvent(listOfEvents.get(position));
        intent.putExtra("event", eventParcel);
        intent.putExtra("plannerId", userID);
        startActivity(intent);
    }

    @Override
    public void onDeleteBtnClick(int position) {
        try {
            eventManager.deleteEvent(listOfEvents.get(position));
            refreshEvents(position);
            if (eventsRecViewAdapter.getItemCount() == 0) {
                showEmptyText();
            }
        } catch (EventNotDeletedException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshEvents(int positionToModify) {
        listOfEvents = eventManager.getAllEvents(userID);
        eventsRecViewAdapter.setEventList(listOfEvents);
        if (positionToModify == -1) {
            eventsRecViewAdapter.notifyDataSetChanged();
        } else {
            eventsRecViewAdapter.notifyItemRemoved(positionToModify);
        }
        showOrHideNoMoreEventsText();
    }

    private void showEmptyText() {
        noMoreEvents.setVisibility(View.VISIBLE);
    }

    private void showOrHideNoMoreEventsText() {
        if (eventsRecViewAdapter.getItemCount() == 0) {
            showEmptyText();
        } else {
            hideEmptyText();
        }
    }

    private void hideEmptyText() {
        noMoreEvents.setVisibility(View.GONE);
    }

    @Override
    public void onEditBtnClick(int position) {
        Intent editActivityIntent = new Intent(this, EditEventActivity.class);
        ParcelableEvent eventParcel = new ParcelableEvent(listOfEvents.get(position));
        editActivityIntent.putExtra("event", eventParcel);
        modifyPersistenceActivityResultLauncher.launch(editActivityIntent);
    }

    // Handle notification click
    private void showInvoiceDialog(ParcelableInvoice invoice) {
        // Create and show the dialog fragment
        InvoiceFragment dialogFragment = InvoiceFragment.newInstance(invoice);
        dialogFragment.show(getSupportFragmentManager(), "InvoiceDialogFragment");
    }

    public void goToPlannerProfileActivity(View view) {
        Intent plannerProfile = new Intent(this, PlannerProfileActivity.class);
        startActivity(plannerProfile);
    }

    public void openVendorList(View v) {
        Intent intent = new Intent(this, VendorBrowseActivity.class);
        intent.putExtra("plannerId", userID);
        startActivity(intent);
    }

}