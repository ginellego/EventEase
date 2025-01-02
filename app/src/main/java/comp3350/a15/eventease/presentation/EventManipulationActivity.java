package comp3350.a15.eventease.presentation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.objects.factory.IEventFactory;

public abstract class EventManipulationActivity extends AppCompatActivity {
    private final Calendar calendar = Calendar.getInstance();
    private final Map<String, TextView> inputFields = new HashMap<>();
    @Inject
    protected IEventManager eventManager;
    @Inject
    protected IUserManager userManager;
    @Inject
    protected IEventFactory eventFactory;
    protected TextView staticPageHeaderEventName;
    protected TextView eventName;
    protected TextView eventDescription;
    protected TextView eventDate;
    protected TextView eventLocation;
    protected TextView eventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_manipulation);

        staticPageHeaderEventName = findViewById(R.id.static_event_name);
        inputFields.put("event name", (TextView) findViewById(R.id.new_event_name));
        inputFields.put("event description", (TextView) findViewById(R.id.new_event_description));
        inputFields.put("event date", (TextView) findViewById(R.id.new_event_date));
        inputFields.put("event location", (TextView) findViewById(R.id.new_event_location));
        inputFields.put("event time", (TextView) findViewById(R.id.new_event_time));
        eventName = inputFields.get("event name");
        eventDescription = inputFields.get("event description");
        eventLocation = inputFields.get("event location");
        eventDate = inputFields.get("event date");
        eventTime = inputFields.get("event time");

        // modify the static text at the top of the page to reflect the name of the
        // event that is being/has been set
        EventManipulationActivity.setStaticPageHeaderEventName(eventName, staticPageHeaderEventName);

        setupDateOnClickListener();
        setupTimeOnClickListener();
    }

    private static void setStaticPageHeaderEventName(TextView eventNameTextView, TextView staticPageHeaderEventName) {
        eventNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Nothing to be done here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                staticPageHeaderEventName.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Nothing to be done here
            }
        });
    }

    private void setupDateOnClickListener() {
        eventDate.setOnClickListener(view -> showDatePickerDialog());
    }

    private void setupTimeOnClickListener() {
        eventTime.setOnClickListener(view -> showTimePickerDialog());
    }

    private void showDatePickerDialog() {
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH);
        int currDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, year, month, dayOfMonth) -> updateDateInView(year, month, dayOfMonth),
                currYear,
                currMonth,
                currDayOfMonth
        );

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Get the current time
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (timePicker, hourOfDay1, minute1) -> updateTimeInView(hourOfDay1, minute1),
                hourOfDay,
                minute,
                android.text.format.DateFormat.is24HourFormat(this)
        );

        timePickerDialog.show();
    }

    private void updateDateInView(int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // Format the selected date and update the TextView
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        eventDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateTimeInView(int hourOfDay, int minute) {
        // Update the TextView with the selected time
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        // Format the time and update the TextView
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(this);
        eventTime.setText(timeFormat.format(calendar.getTime()));
    }

    public void returnToMainActivity(View view) {
        setResult(RESULT_OK);
        finish();
    }

    protected void setEmptyFieldError() {
        for (Map.Entry<String, TextView> entry : inputFields.entrySet()) {
            TextView textView = entry.getValue();
            if (textView.getText() == null || textView.getText().toString().trim().isEmpty())
                textView.setError("Please enter the " + entry.getKey());
        }
    }
}
