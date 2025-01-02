package comp3350.a15.eventease.presentation.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.presentation.adapter.recyclerInterface.IEventsRecView;


public class EventsRecViewAdapter extends RecyclerView.Adapter<EventsRecViewAdapter.ViewHolder> {
    private final IEventsRecView eventsRecViewInterface;
    private List<Event> eventList;


    public EventsRecViewAdapter(List<Event> allEvents, IEventsRecView eventsRecViewInterface) {
        super();
        this.eventsRecViewInterface = eventsRecViewInterface;
        eventList = allEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_item, parent, false);
        return new ViewHolder(view, eventsRecViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventName.setText(eventList.get(position).getEventName());
        holder.eventDescription.setText(eventList.get(position).getEventDescription());
        holder.eventDate.setText(eventList.get(position).getEventDate().toString());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventName;
        private final TextView eventDate;
        private final TextView eventDescription;

        public ViewHolder(@NonNull View itemView, IEventsRecView eventsRecViewInterface) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            FloatingActionButton delete = itemView.findViewById(R.id.delete_event);
            FloatingActionButton edit = itemView.findViewById(R.id.edit_event);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && eventsRecViewInterface != null) {
                    eventsRecViewInterface.onEventCardClick(position);
                }
            });

            delete.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && eventsRecViewInterface != null) {
                    eventsRecViewInterface.onDeleteBtnClick(position);
                }
            });

            edit.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION && eventsRecViewInterface != null) {
                    eventsRecViewInterface.onEditBtnClick(position);
                }
            });
        }
    }
}
