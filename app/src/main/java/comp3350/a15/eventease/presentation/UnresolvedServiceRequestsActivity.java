package comp3350.a15.eventease.presentation;

import static comp3350.a15.eventease.presentation.SendNotification.sendNotificationToPlanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.application.qualifier.AcceptedServiceRequest;
import comp3350.a15.eventease.application.qualifier.RejectedServiceRequest;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.presentation.adapter.UnresolvedRequestsRecViewAdapter;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IRequestsRecView;
import comp3350.a15.eventease.presentation.enums.RequestStatusCode;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UnresolvedServiceRequestsActivity extends AppCompatActivity implements IRequestsRecView {
    private static int CURRENT_REQUEST_POSITION = -1;
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
    private UnresolvedRequestsRecViewAdapter unresolvedRequestsRecViewAdapter;
    private TextView noMoreRequests;
    private List<ServiceRequest> listOfUnresolvedRequests;
    private int userId;
    private final ActivityResultLauncher<Intent> respondToRequestLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> refreshRequests(result.getResultCode()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unresolved_service_requests);

        RecyclerView requestsRecView = findViewById(R.id.requestsRecView);
        noMoreRequests = findViewById(R.id.no_more_requests);

        userId = getIntent().getIntExtra("userId", 0);
        listOfUnresolvedRequests = unresolvedServiceRequestManager.getAllRequests(userId);
        unresolvedRequestsRecViewAdapter = new UnresolvedRequestsRecViewAdapter(listOfUnresolvedRequests, this);

        requestsRecView.setAdapter(unresolvedRequestsRecViewAdapter);
        requestsRecView.setLayoutManager(new LinearLayoutManager(this));

        showOrHideNoMoreRequestsText();
    }

    private void showOrHideNoMoreRequestsText() {
        if (unresolvedRequestsRecViewAdapter.getItemCount() == 0) {
            showEmptyText();
        } else {
            hideEmptyText();
        }
    }

    private void showEmptyText() {
        noMoreRequests.setVisibility(View.VISIBLE);
    }

    private void hideEmptyText() {
        noMoreRequests.setVisibility(View.GONE);
    }

    @Override
    public void onRequestClick(int position) {
        CURRENT_REQUEST_POSITION = position;
        ServiceRequest currentRequest = listOfUnresolvedRequests.get(position);
        ParcelableServiceRequest requestParcel = new ParcelableServiceRequest(currentRequest);

        Intent intent = new Intent(this, ServiceRequestDetailsActivity.class);

        intent.putExtra("request", requestParcel);
        respondToRequestLauncher.launch(intent);

        unresolvedServiceRequestManager.setServiceStatus(currentRequest.getId(), ServiceRequest.ServiceStatus.PENDING);
        refreshRequests(RequestStatusCode.UNRESOLVED.getStatusCode());
    }

    @Override
    public void onRequestLongClick(int position) {
        CURRENT_REQUEST_POSITION = position;
        ServiceRequest request = listOfUnresolvedRequests.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("What would you like to do with this request?").
                setPositiveButton("Accept", (dialog, which) -> {
                    unresolvedServiceRequestManager.setServiceStatus(request.getId(), ServiceRequest.ServiceStatus.ACCEPTED);
                    refreshRequests(RequestStatusCode.ACCEPTED.getStatusCode());

                    //generate and store invoice
                    int invoiceId = invoiceManager.generateInvoice(request);
                    //Send notification to planner about accepted request
                    sendNotificationToPlanner(this, Optional.of(invoiceId), null, "acceptedServiceRequest");
                })
                .setNegativeButton("Decline", (dialog, which) -> {
                    unresolvedServiceRequestManager.setServiceStatus(request.getId(), ServiceRequest.ServiceStatus.REJECTED);
                    refreshRequests(RequestStatusCode.REJECTED.getStatusCode());

                    //Send notification to planner about rejected request
                    sendNotificationToPlanner(this, Optional.empty(), request, "rejectedServiceRequest");
                }).setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
    }

    private void refreshRequests(int resultCode) {
        if (CURRENT_REQUEST_POSITION != -1) {
            listOfUnresolvedRequests = unresolvedServiceRequestManager.getAllRequests(userId);
            unresolvedRequestsRecViewAdapter.setRequestsList(listOfUnresolvedRequests);
            if (resultCode == RequestStatusCode.UNRESOLVED.getStatusCode()) {
                unresolvedRequestsRecViewAdapter.notifyItemChanged(CURRENT_REQUEST_POSITION);
            } else {
                unresolvedRequestsRecViewAdapter.notifyItemRemoved(CURRENT_REQUEST_POSITION);
                if (resultCode == RequestStatusCode.ACCEPTED.getStatusCode()) {
                    setResult(resultCode);
                }
            }
        }
        showOrHideNoMoreRequestsText();
    }
}
