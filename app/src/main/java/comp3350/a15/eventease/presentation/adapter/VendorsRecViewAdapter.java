package comp3350.a15.eventease.presentation.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IVendorRecView;

public class VendorsRecViewAdapter extends RecyclerView.Adapter<VendorsRecViewAdapter.ViewHolder> {

    private final IVendorRecView vendorRecViewInterface;
    private List<Vendor> vendorList;

    private final Event currEvent;

    final IServiceRequestManager unresolvedRequestManager;

    public VendorsRecViewAdapter(IServiceRequestManager unresolvedRequestManager, Event event, List<Vendor> allVendors, IVendorRecView vendorsRecViewInterface) {
        super();
        this.vendorRecViewInterface = vendorsRecViewInterface;
        this.vendorList = allVendors;
        this.currEvent = event;
        this.unresolvedRequestManager = unresolvedRequestManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_item, parent, false);
        return new VendorsRecViewAdapter.ViewHolder(view, vendorRecViewInterface, vendorList);
    }

    @Override
    public void onBindViewHolder(@NonNull comp3350.a15.eventease.presentation.adapter.VendorsRecViewAdapter.ViewHolder holder, int position) {
        holder.vendorName.setText(vendorList.get(position).getVendorName());
        holder.vendorServiceType.setText(vendorList.get(position).getServiceType());
        holder.vendorDescription.setText(vendorList.get(position).getVendorDescription());
        holder.vendorRating.setText(vendorList.get(position).getRating());
        holder.vendorCost.setText(String.valueOf(vendorList.get(position).getCost()));
        holder.vendorPhone.setText(vendorList.get(position).getVendorNumber());
        holder.vendorEmail.setText(vendorList.get(position).getVendorEmail());
        holder.vendorPicture.setImageResource(vendorList.get(position).getVendorPictures());

        Vendor currVendor = vendorList.get(position);
        boolean requestSent = unresolvedRequestManager.doesRequestExist(new ServiceRequest(currEvent, currVendor.getAccountId(), currVendor.getServiceType(), currEvent.getEventDate(), currVendor.getCost(), ServiceRequest.ServiceStatus.NEW)) ||
                unresolvedRequestManager.doesRequestExist(new ServiceRequest(currEvent, currVendor.getAccountId(), currVendor.getServiceType(), currEvent.getEventDate(), currVendor.getCost(), ServiceRequest.ServiceStatus.ACCEPTED)) ||
                unresolvedRequestManager.doesRequestExist(new ServiceRequest(currEvent, currVendor.getAccountId(), currVendor.getServiceType(), currEvent.getEventDate(), currVendor.getCost(), ServiceRequest.ServiceStatus.PENDING)) ||
                unresolvedRequestManager.doesRequestExist(new ServiceRequest(currEvent, currVendor.getAccountId(), currVendor.getServiceType(), currEvent.getEventDate(), currVendor.getCost(), ServiceRequest.ServiceStatus.REJECTED));

        if (requestSent) {
            holder.send.setVisibility(View.INVISIBLE);
            holder.send.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        if (vendorList != null) {
            return vendorList.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVendorList(List<Vendor> vendorList) {
        this.vendorList = vendorList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView vendorName;
        private final TextView vendorServiceType;
        private final TextView vendorDescription;
        private final TextView vendorRating;
        private final TextView vendorCost;
        private final TextView vendorPhone;
        private final TextView vendorEmail;
        private final ImageView vendorPicture;
        private final FloatingActionButton send;

        public ViewHolder(@NonNull View itemView, IVendorRecView vendorRecViewInterface, List<Vendor> vendorList) {
            super(itemView);

            vendorName = itemView.findViewById(R.id.vendorName);
            vendorServiceType = itemView.findViewById(R.id.vendorServiceType);
            vendorDescription = itemView.findViewById(R.id.vendorDescription);
            vendorRating = itemView.findViewById(R.id.vendorRating);
            vendorCost = itemView.findViewById(R.id.vendorCost);
            vendorPhone = itemView.findViewById(R.id.vendorPhone);
            vendorEmail = itemView.findViewById(R.id.vendorEmail);
            vendorPicture = itemView.findViewById(R.id.vendor_image);
            send = itemView.findViewById(R.id.saveVendor_button);


            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && vendorRecViewInterface != null) {
                    vendorRecViewInterface.onVendorCardClick(vendorList.get(position).getAccountId());
                }
            });

            send.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && vendorRecViewInterface != null) {
                    vendorRecViewInterface.onSendBtnClick(vendorList.get(position).getAccountId());
                    send.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                }
            });
        }
    }
}
