package comp3350.a15.eventease.presentation.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IDisplayEventRequestsRecView;


public class EventRelatedRequestsRecViewAdapter extends RecyclerView.Adapter<EventRelatedRequestsRecViewAdapter.ViewHolder> {
    private final List<ServiceRequest> requestList;
    private final IDisplayEventRequestsRecView requestsRecViewInterface;

    private final IVendorManager vendorManager;


    public EventRelatedRequestsRecViewAdapter(IVendorManager vendorManager, List<ServiceRequest> allRequests, IDisplayEventRequestsRecView requestsRecViewInterface) {
        super();
        this.requestsRecViewInterface = requestsRecViewInterface;
        this.requestList = allRequests;
        this.vendorManager = vendorManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_service_request_item,
                parent, false);
        return new ViewHolder(view, requestsRecViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRequest request = requestList.get(position);
        holder.vendorName.setText(vendorManager.getVendorByAccountId(request.getVendorID()).getVendorName());
        holder.serviceType.setText(request.getServiceType());

        // Check if the request is unopened
        if (request.getServiceStatus() != ServiceRequest.ServiceStatus.ACCEPTED) {
            holder.invoiceBtn.setVisibility(View.INVISIBLE);
            holder.invoiceBtn.setClickable(false);
        }
        if (request.getServiceStatus() == ServiceRequest.ServiceStatus.REJECTED) {
            holder.vendorName.setPaintFlags(holder.serviceType.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.serviceType.setPaintFlags(holder.serviceType.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (request.getServiceStatus() == ServiceRequest.ServiceStatus.PENDING) {
            holder.blueDotIndicator.setVisibility(View.GONE);
        } else {
            holder.blueDotIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView serviceType;
        private final TextView vendorName;
        private final View blueDotIndicator;

        private final View invoiceBtn;

        public ViewHolder(@NonNull View itemView, IDisplayEventRequestsRecView requestsRecViewInterface) {
            super(itemView);
            vendorName = itemView.findViewById(R.id.vendorName);
            serviceType = itemView.findViewById(R.id.serviceType);
            blueDotIndicator = itemView.findViewById(R.id.blueDotIndicator);
            invoiceBtn = itemView.findViewById(R.id.showInvoiceBtn);

            invoiceBtn.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && requestsRecViewInterface != null)
                    requestsRecViewInterface.onInvoiceBtnClick(position);

            });
        }
    }
}
