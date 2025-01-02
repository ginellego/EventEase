package comp3350.a15.eventease.presentation;

import static comp3350.a15.eventease.presentation.SendNotification.sendNotificationToVendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.logic.exceptions.ServiceRequestException;
import comp3350.a15.eventease.logic.exceptions.VendorException;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;
import comp3350.a15.eventease.presentation.adapter.VendorsAllRecViewAdapter;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IVendorAllRecView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorBrowseActivity extends AppCompatActivity implements IVendorAllRecView {

    @Inject
    IVendorManager vendorManager;
    @Inject
    @UnresolvedServiceRequest
    IServiceRequestManager unresolvedRequestManager;
    @Inject
    IEventManager eventManager;
    private ArrayList<Vendor> vendors;
    private List<Event> events;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);
        int userID = getIntent().getIntExtra("plannerId", -1);
        try {
            vendors = vendorManager.getAllVendors();
        } catch (VendorException e) {
            Toast.makeText(this, "Unable to launch, please try again", Toast.LENGTH_SHORT).show();
            finish();
        }
        try {
            events = eventManager.getAllEvents(userID);
        } catch (PersistenceException e) {
            Toast.makeText(this, "Unable to launch, please try again", Toast.LENGTH_SHORT).show();
            finish();
        }

        RecyclerView vendorsRecView = findViewById(R.id.vendorRecView);

        VendorsAllRecViewAdapter vendorsRecViewAdapter = new VendorsAllRecViewAdapter(events, vendors, this);
        vendorsRecView.setAdapter(vendorsRecViewAdapter);
        vendorsRecView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void setEvent(Event event) {
        this.event = event;
    }

    public void returnToMainActivity(View view) {
        Intent goToMainActivity = new Intent(this, PlannerMainActivity.class);
        startActivity(goToMainActivity);
    }

    @Override
    public void onVendorCardClick(int position) {

        Intent intent = new Intent(this, VendorProfileActivity.class);
        intent.putExtra("vendor", new ParcelableVendor(vendorManager.getVendorByAccountId(position)));
        startActivity(intent);
    }

    @Override
    public void onSendBtnClick(int position) {
        Vendor selected = vendorManager.getVendorByAccountId(position);
        try {
            unresolvedRequestManager.addNewRequest(event, selected.getAccountId(), selected.getServiceType(),
                    event.getEventDate(), selected.getCost());
            sendNotificationToVendor(this, selected.getAccountId());
            Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
        } catch (ServiceRequestException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void goBackToPlannerMain(View view) {
        Intent intent = new Intent(this, PlannerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
    }
}
