package comp3350.a15.eventease.presentation;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.exceptions.CantModifyEventException;
import comp3350.a15.eventease.logic.exceptions.EventAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.EventDateInThePastException;
import comp3350.a15.eventease.logic.exceptions.EventNotEditedException;
import comp3350.a15.eventease.objects.Event;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditEventActivity extends EventManipulationActivity {
    private Event eventToBeEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventToBeEdited = getIntent().getParcelableExtra("event", ParcelableEvent.class).toEvent(eventManager);

        if (eventToBeEdited != null) {
            Button createButton = findViewById(R.id.create_button);
            createButton.setText(R.string.save);
            createButton.setOnClickListener(this::editEvent);
            populateUI(eventToBeEdited);
        }
    }

    public void editEvent(View view) {
        CharSequence name = eventName.getText();
        CharSequence description = eventDescription.getText();
        CharSequence location = eventLocation.getText();
        CharSequence date = eventDate.getText();
        CharSequence time = eventTime.getText();

        try {
            eventManager.editEvent(eventToBeEdited, name, description, location, date, time);
            Toast.makeText(this, "Event edited successfully", Toast.LENGTH_SHORT).show();
            returnToMainActivity(view);
        } catch (EventNotEditedException e) {
            setEmptyFieldError();
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } catch (EventAlreadyExistsException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (EventDateInThePastException e) {
            eventDate.setError("Event date cannot be in the past");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (CantModifyEventException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            returnToMainActivity(view);
        }
    }

    private void populateUI(Event selectedEvent) {
        eventName.setText(selectedEvent.getEventName());
        eventDescription.setText(selectedEvent.getEventDescription());
        eventLocation.setText(selectedEvent.getEventLocation());
        eventDate.setText(selectedEvent.getEventDate().toString());
        eventTime.setText(selectedEvent.getEventTime().toString());
    }
}