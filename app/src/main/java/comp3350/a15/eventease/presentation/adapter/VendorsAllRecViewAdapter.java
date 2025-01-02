package comp3350.a15.eventease.presentation.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IVendorAllRecView;

public class VendorsAllRecViewAdapter extends RecyclerView.Adapter<VendorsAllRecViewAdapter.ViewHolder> {

    private final List<Vendor> vendorList;
    private final List<Event> eventList;
    private final IVendorAllRecView vendorRecViewInterface;

    public VendorsAllRecViewAdapter(List<Event> allEvents, List<Vendor> allVendors, IVendorAllRecView vendorsRecViewInterface) {
        super();
        this.vendorRecViewInterface = vendorsRecViewInterface;
        this.vendorList = allVendors;
        this.eventList = allEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_all_list_item, parent, false);
        return new VendorsAllRecViewAdapter.ViewHolder(view, vendorRecViewInterface, eventList, vendorList);
    }

    @Override
    public void onBindViewHolder(@NonNull comp3350.a15.eventease.presentation.adapter.VendorsAllRecViewAdapter.ViewHolder holder, int position) {
        holder.vendorName.setText(vendorList.get(position).getVendorName());
        holder.vendorServiceType.setText(vendorList.get(position).getServiceType());
        holder.vendorDescription.setText(vendorList.get(position).getVendorDescription());
        holder.vendorRating.setText(vendorList.get(position).getRating());
        holder.vendorCost.setText(String.valueOf(vendorList.get(position).getCost()));
        holder.vendorPhone.setText(vendorList.get(position).getVendorNumber());
        holder.vendorEmail.setText(vendorList.get(position).getVendorEmail());
        holder.vendorPicture.setImageResource(vendorList.get(position).getVendorPictures());
    }

    @Override
    public int getItemCount() {
        if (vendorList != null) {
            return vendorList.size();
        }
        return 0;
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
        private final Spinner eventDropdown;

        public ViewHolder(@NonNull View itemView, IVendorAllRecView vendorRecViewInterface, List<Event> eventList, List<Vendor> vendorList) {
            super(itemView);

            vendorName = itemView.findViewById(R.id.vendorName);
            vendorServiceType = itemView.findViewById(R.id.vendorServiceType);
            vendorDescription = itemView.findViewById(R.id.vendorDescription);
            vendorRating = itemView.findViewById(R.id.vendorRating);
            vendorCost = itemView.findViewById(R.id.vendorCost);
            vendorPhone = itemView.findViewById(R.id.vendorPhone);
            vendorEmail = itemView.findViewById(R.id.vendorEmail);
            vendorPicture = itemView.findViewById(R.id.vendor_image);
            FloatingActionButton send = itemView.findViewById(R.id.saveVendor_button);
            eventDropdown = itemView.findViewById(R.id.eventDropdown);
            List<String> eventNames = new ArrayList<>();
            eventNames.add("Please select an event to send a request");
            for (Event event : eventList) {
                eventNames.add(event.getEventName());
            }

            ArrayAdapter<String> adapter = getArrayAdapter(itemView, eventNames);
            eventDropdown.setAdapter(adapter);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && vendorRecViewInterface != null) {
                    vendorRecViewInterface.onVendorCardClick(vendorList.get(position).getAccountId());
                }
            });

            if (!eventList.isEmpty()) {
                eventDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String value = parent.getItemAtPosition(position).toString();
                        if (value.equals(eventNames.get(0))) {
                            ((TextView) view).setTextColor(Color.GRAY);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //Do nothing
                    }
                });

                send.setOnClickListener(v -> {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && vendorRecViewInterface != null) {
                        int dropDownSelected;
                        if ((dropDownSelected = eventDropdown.getSelectedItemPosition()) > 0) {
                            Event selected = eventList.get(dropDownSelected - 1);
                            vendorRecViewInterface.setEvent(selected);
                            vendorRecViewInterface.onSendBtnClick(vendorList.get(position).getAccountId());
                        } else {
                            Toast.makeText(v.getContext(), "Please select an event to send a request", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                send.setVisibility(View.INVISIBLE);
                send.setEnabled(false);
                eventDropdown.setVisibility(View.INVISIBLE);
                eventDropdown.setEnabled(false);
            }
        }

        @NonNull
        private static ArrayAdapter<String> getArrayAdapter(@NonNull View itemView, List<String> eventNames) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, eventNames) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                    if (position == 0) {
                        view.setTextColor(Color.GRAY);
                    }
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return adapter;
        }
    }

}
