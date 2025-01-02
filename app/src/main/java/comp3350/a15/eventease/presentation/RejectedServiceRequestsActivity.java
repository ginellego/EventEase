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
import comp3350.a15.eventease.application.qualifier.RejectedServiceRequest;
import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.presentation.adapter.RejectedRequestsRecViewAdapter;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IRequestsRecView;
import comp3350.a15.eventease.presentation.enums.RequestStatusCode;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RejectedServiceRequestsActivity extends AppCompatActivity implements IRequestsRecView {
    private static int CURRENT_REQUEST_POSITION = -1;
    @Inject
    @RejectedServiceRequest
    IServiceRequestManager rejectedServiceRequestManager;

    @Inject
    IRequestInvoiceManager invoiceManager;

    private RejectedRequestsRecViewAdapter rejectedRequestsRecViewAdapter;

    private List<ServiceRequest> listOfRejectedRequests;
    private TextView noMoreRequests;
    private int userId;
    private final ActivityResultLauncher<Intent> respondToRequestLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> refreshRequests(result.getResultCode()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_service_requests);

        RecyclerView requestsRecView = findViewById(R.id.r_requestsRecView);
        noMoreRequests = findViewById(R.id.r_no_more_requests);

        userId = getIntent().getIntExtra("userId", 0);
        listOfRejectedRequests = rejectedServiceRequestManager.getAllRequests(userId);
        rejectedRequestsRecViewAdapter = new RejectedRequestsRecViewAdapter(listOfRejectedRequests, this);

        requestsRecView.setAdapter(rejectedRequestsRecViewAdapter);
        requestsRecView.setLayoutManager(new LinearLayoutManager(this));

        showOrHideNoMoreRequestsText();
    }

    private void showOrHideNoMoreRequestsText() {
        if (rejectedRequestsRecViewAdapter.getItemCount() == 0) {
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
        ServiceRequest currentRequest = listOfRejectedRequests.get(position);
        ParcelableServiceRequest requestParcel = new ParcelableServiceRequest(currentRequest);

        Intent intent = new Intent(this, ServiceRequestDetailsActivity.class);
        intent.putExtra("request", requestParcel);
        respondToRequestLauncher.launch(intent);
    }

    @Override
    public void onRequestLongClick(int position) {
        CURRENT_REQUEST_POSITION = position;
        ServiceRequest request = listOfRejectedRequests.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("What would you like to do with this request?").
                setPositiveButton("Accept", (dialog, which) -> {
                    rejectedServiceRequestManager.setServiceStatus(request.getId(), ServiceRequest.ServiceStatus.ACCEPTED);
                    refreshRequests(RequestStatusCode.ACCEPTED.getStatusCode());

                    int invoiceId = invoiceManager.generateInvoice(request);
                    sendNotificationToPlanner(this, Optional.of(invoiceId), null, "acceptedServiceRequest");
                })
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
    }

    private void refreshRequests(int resultCode) {
        if (CURRENT_REQUEST_POSITION != -1) {
            listOfRejectedRequests = rejectedServiceRequestManager.getAllRequests(userId);
            rejectedRequestsRecViewAdapter.setRequestsList(listOfRejectedRequests);
            if (resultCode == RequestStatusCode.ACCEPTED.getStatusCode()) {
                rejectedRequestsRecViewAdapter.notifyItemRemoved(CURRENT_REQUEST_POSITION);
            } else {
                rejectedRequestsRecViewAdapter.notifyItemChanged(CURRENT_REQUEST_POSITION);
            }
            if (resultCode == RequestStatusCode.ACCEPTED.getStatusCode()) {
                setResult(resultCode);
            }
        }
        showOrHideNoMoreRequestsText();
    }
}
