package comp3350.a15.eventease.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.application.qualifier.AcceptedServiceRequest;
import comp3350.a15.eventease.application.qualifier.RejectedServiceRequest;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.logic.exceptions.EventNotFoundException;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;
import comp3350.a15.eventease.presentation.adapter.EventRelatedRequestsRecViewAdapter;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IDisplayEventRequestsRecView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DisplayEventActivity extends AppCompatActivity implements IDisplayEventRequestsRecView {
    @Inject
    IEventManager eventManager;

    @Inject
    IVendorManager vendorManager;
    @Inject
    @UnresolvedServiceRequest
    IServiceRequestManager unresolvedRequestManager;

    @Inject
    @AcceptedServiceRequest
    IServiceRequestManager acceptedServiceRequestManager;

    @Inject
    @RejectedServiceRequest
    IServiceRequestManager rejectedServiceRequestManager;


    @Inject
    IRequestInvoiceManager invoiceManager;

    private Event currEvent;

    private List<ServiceRequest> listOfServiceReqs;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event);

        try {
            currEvent = getIntent().getParcelableExtra("event", ParcelableEvent.class).toEvent(eventManager);
            listOfServiceReqs = unresolvedRequestManager.getAllRequestsOfEvent(currEvent.getId());

            userId = getIntent().getIntExtra("plannerId", -1);

            TextView eventName = findViewById(R.id.event_title);
            TextView eventDescription = findViewById(R.id.event_description);
            TextView eventLocation = findViewById(R.id.event_location);
            TextView eventDate = findViewById(R.id.event_date);
            TextView eventTime = findViewById(R.id.event_time);

            RecyclerView requestsRecView = findViewById(R.id.requestsRecViewUnderEvent);
            EventRelatedRequestsRecViewAdapter requestsRecViewAdapter = new EventRelatedRequestsRecViewAdapter(vendorManager, listOfServiceReqs, this);
            requestsRecView.setAdapter(requestsRecViewAdapter);
            requestsRecView.setLayoutManager(new LinearLayoutManager(this));

            if (currEvent != null) {
                eventName.setText(currEvent.getEventName());
                eventDescription.setText(currEvent.getEventDescription());
                eventLocation.setText(currEvent.getEventLocation());
                eventDate.setText(currEvent.getEventDate().toString());
                eventTime.setText(currEvent.getEventTime().toString());
            }

        } catch (EventNotFoundException e) {
            Toast.makeText(this, "The event no longer exists", Toast.LENGTH_SHORT).show();
            finish();
        } catch (PersistenceException e) {
            Toast.makeText(this, "Unable to launch please try again", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void returnToMainActivity(View view) {
        finish();
    }

    public void goToVendorCategories(View view) {
        Intent goToVendorCategoryActivity = new Intent(this, VendorCategoryActivity.class);
        ParcelableEvent eventParcel = new ParcelableEvent(currEvent);
        goToVendorCategoryActivity.putExtra("event", eventParcel);
        goToVendorCategoryActivity.putExtra("plannerId", userId);
        startActivity(goToVendorCategoryActivity);
    }

    @Override
    public void onInvoiceBtnClick(int position) {
        ServiceRequest request = listOfServiceReqs.get(position);
        try {
            Invoice invoice = invoiceManager.getInvoiceByRequestId(request.getId());
            ParcelableInvoice invoiceParcel = new ParcelableInvoice(invoice);

            InvoiceFragment dialogFragment = InvoiceFragment.newInstance(invoiceParcel);
            dialogFragment.show(getSupportFragmentManager(), "InvoiceDialogFragment");
        } catch (PersistenceException e) {
            Toast.makeText(this, "Invoice not found", Toast.LENGTH_LONG).show();
        }
    }
}