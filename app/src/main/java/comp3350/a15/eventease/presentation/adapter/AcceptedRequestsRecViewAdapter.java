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
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IAcceptedRequestsRecView;


public class AcceptedRequestsRecViewAdapter extends RecyclerView.Adapter<AcceptedRequestsRecViewAdapter.ViewHolder> {
    private final IAcceptedRequestsRecView requestsRecViewInterface;
    private List<ServiceRequest> listOfRequests;


    public AcceptedRequestsRecViewAdapter(List<ServiceRequest> listOfRequests, IAcceptedRequestsRecView requestsRecViewInterface) {
        super();
        this.requestsRecViewInterface = requestsRecViewInterface;
        this.listOfRequests = listOfRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list_item, parent, false);
        return new ViewHolder(view, requestsRecViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.serviceType.setText(listOfRequests.get(position).getServiceType());
        holder.eventDescription.setText(listOfRequests.get(position).getAssociatedEvent().getEventDescription());
        holder.deadline.setText(listOfRequests.get(position).getAssociatedEvent().getEventDate().toString());
    }

    @Override
    public int getItemCount() {
        return listOfRequests.size();
    }

    public void setRequestsList(List<ServiceRequest> listOfAcceptedRequests) {
        listOfRequests = listOfAcceptedRequests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView serviceType;
        private final TextView deadline;
        private final TextView eventDescription;

        public ViewHolder(@NonNull View itemView, IAcceptedRequestsRecView requestsRecViewInterface) {
            super(itemView);
            serviceType = itemView.findViewById(R.id.eventName);
            deadline = itemView.findViewById(R.id.eventDate);
            eventDescription = itemView.findViewById(R.id.eventDescription);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && requestsRecViewInterface != null) {
                    requestsRecViewInterface.onRequestCardClick(position);
                }
            });


        }
    }
}
