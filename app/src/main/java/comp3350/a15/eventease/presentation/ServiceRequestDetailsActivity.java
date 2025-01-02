package comp3350.a15.eventease.presentation;

import static comp3350.a15.eventease.presentation.SendNotification.sendNotificationToPlanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.application.qualifier.AcceptedServiceRequest;
import comp3350.a15.eventease.application.qualifier.RejectedServiceRequest;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.presentation.enums.RequestStatusCode;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServiceRequestDetailsActivity extends AppCompatActivity {
    @Inject
    @UnresolvedServiceRequest
    IServiceRequestManager unresolvedServiceRequestManager;

    @Inject
    @AcceptedServiceRequest
    IServiceRequestManager acceptedServiceRequestManager;

    @Inject
    @RejectedServiceRequest
    IServiceRequestManager rejectedServiceRequestManager;

    @Inject
    IRequestInvoiceManager invoiceManager;

    private ServiceRequest currRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request_details);

        currRequest = getIntent().getParcelableExtra("request", ParcelableServiceRequest.class).toServiceRequest(unresolvedServiceRequestManager);
        TextView serviceType = findViewById(R.id.service_type);
        TextView eventName = findViewById(R.id.request_event_name);
        TextView eventDescription = findViewById(R.id.request_event_description);
        TextView eventLocation = findViewById(R.id.request_event_location);
        TextView eventDate = findViewById(R.id.request_event_date);
        TextView eventTime = findViewById(R.id.request_event_time);
        TextView requestBudget = findViewById(R.id.request_event_budget);

        if (currRequest != null) {
            serviceType.setText(currRequest.getServiceType());
            eventName.setText(currRequest.getAssociatedEvent().getEventName());
            eventDescription.setText(currRequest.getAssociatedEvent().getEventDescription());
            eventLocation.setText(currRequest.getAssociatedEvent().getEventLocation());
            eventDate.setText(currRequest.getAssociatedEvent().getEventDate().toString());
            eventTime.setText(currRequest.getAssociatedEvent().getEventTime().toString());
            String budget = "$" + currRequest.getBudget();
            requestBudget.setText(budget);

            switch (currRequest.getServiceStatus()) {
                case ACCEPTED:
                case REJECTED:
                    findViewById(R.id.btn_reject_request).setVisibility(View.GONE);
                    findViewById(R.id.btn_accept_request).setVisibility(View.GONE);
                    break;
                default:
                    findViewById(R.id.btn_reject_request).setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_accept_request).setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public void acceptRequest(View view) {
        addRequest();
    }

    public void addRequest() {
        unresolvedServiceRequestManager.setServiceStatus(currRequest.getId(), ServiceRequest.ServiceStatus.ACCEPTED);
        setResult(RequestStatusCode.ACCEPTED.getStatusCode());

        int invoiceId = invoiceManager.generateInvoice(currRequest);
        //Send notification to planner about accepted request
        sendNotificationToPlanner(this, Optional.of(invoiceId), null, "acceptedServiceRequest");
        finish();
    }

    public void declineRequest(View view) {
        AlertDialog.Builder declineDialog = new AlertDialog.Builder(this);
        declineDialog.setTitle("Are you sure you want to decline this request?")
                .setMessage("Declined requests can be re-accepted later.")
                .setPositiveButton("Yes", (dialog, which) -> removeRequest())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        declineDialog.show();
    }

    private void removeRequest() {
        unresolvedServiceRequestManager.setServiceStatus(currRequest.getId(), ServiceRequest.ServiceStatus.REJECTED);
        setResult(RequestStatusCode.REJECTED.getStatusCode());

        sendNotificationToPlanner(this, Optional.empty(), currRequest, "rejectedServiceRequest");
        finish();
    }

}