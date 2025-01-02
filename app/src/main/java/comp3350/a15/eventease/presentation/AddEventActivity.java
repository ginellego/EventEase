package comp3350.a15.eventease.presentation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import comp3350.a15.eventease.logic.exceptions.EventAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.EventDateInThePastException;
import comp3350.a15.eventease.logic.exceptions.EventNotAddedException;
import comp3350.a15.eventease.logic.exceptions.FieldMissingException;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEventActivity extends EventManipulationActivity {
    private static final String USER_LOGIN_KEY = "userLogin";
    private static final String USER_PREFS = "userPreferences";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    public void createNewEvent(View view) {
        CharSequence name = eventName.getText();
        CharSequence description = eventDescription.getText();
        CharSequence location = eventLocation.getText();
        CharSequence date = eventDate.getText();
        CharSequence time = eventTime.getText();

        try {
            String username = sharedPreferences.getString(USER_LOGIN_KEY, "");
            int userId = userManager.getUserId(username);
            eventManager.addNewEvent(userId, name, description, location, date, time);
            Toast.makeText(this, "Event added successfully", Toast.LENGTH_LONG).show();
            returnToMainActivity(view);
        } catch (FieldMissingException e) {
            setEmptyFieldError();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (EventNotAddedException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (EventAlreadyExistsException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (EventDateInThePastException e) {
            eventDate.setError("Event date cannot be in the past");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}