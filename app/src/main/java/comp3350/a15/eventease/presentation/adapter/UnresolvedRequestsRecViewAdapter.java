package comp3350.a15.eventease.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IRequestsRecView;


public class UnresolvedRequestsRecViewAdapter extends RecyclerView.Adapter<UnresolvedRequestsRecViewAdapter.ViewHolder> {
    private final IRequestsRecView requestsRecViewInterface;
    private List<ServiceRequest> requestList;


    public UnresolvedRequestsRecViewAdapter(List<ServiceRequest> allRequests, IRequestsRecView requestsRecViewInterface) {
        super();
        this.requestsRecViewInterface = requestsRecViewInterface;
        requestList = allRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_request_item,
                parent, false);
        return new ViewHolder(view, requestsRecViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.serviceType.setText(requestList.get(position).getServiceType());
        holder.eventDescription.setText(requestList.get(position).getAssociatedEvent().getEventDescription());
        holder.serviceDate.setText(requestList.get(position).getDeadline().toString());

        // Check if the request is unopened
        ServiceRequest request = requestList.get(position);
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

    public void setRequestsList(List<ServiceRequest> listOfUnresolvedRequests) {
        requestList = listOfUnresolvedRequests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView serviceType;
        private final TextView serviceDate;
        private final TextView eventDescription;
        private final View blueDotIndicator;

        public ViewHolder(@NonNull View itemView, IRequestsRecView requestsRecViewInterface) {
            super(itemView);
            serviceType = itemView.findViewById(R.id.serviceType);
            serviceDate = itemView.findViewById(R.id.serviceDate);
            eventDescription = itemView.findViewById(R.id.event_description);
            blueDotIndicator = itemView.findViewById(R.id.blueDotIndicator);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && requestsRecViewInterface != null) {
                    requestsRecViewInterface.onRequestClick(position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && requestsRecViewInterface != null) {
                    requestsRecViewInterface.onRequestLongClick(position);
                }
                return true;
            });
        }
    }
}
