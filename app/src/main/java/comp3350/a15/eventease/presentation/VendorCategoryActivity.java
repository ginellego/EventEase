package comp3350.a15.eventease.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.logic.exceptions.EmptyVendorCategoryException;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Vendor;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorCategoryActivity extends AppCompatActivity {


    @Inject
    IVendorManager vendorManager;

    @Inject
    IEventManager eventManager;

    private Event currEvent;

    private ArrayList<String> selectedCategories;
    private CheckBox venue, cake, catering, music, decor, floral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_category);

        selectedCategories = new ArrayList<>();
        venue = findViewById(R.id.venue);
        cake = findViewById(R.id.cake);
        catering = findViewById(R.id.catering);
        music = findViewById(R.id.music);
        decor = findViewById(R.id.decor);
        floral = findViewById(R.id.florist);

        currEvent = getIntent().getParcelableExtra("event", ParcelableEvent.class).toEvent(eventManager);

        Button button = findViewById(R.id.get_vendors);
        button.setOnClickListener(this::getVendors);
    }

    public void getVendors(View view) {
        if (venue.isChecked()) {
            selectedCategories.add("Venue");
        }
        if (cake.isChecked()) {
            selectedCategories.add("Cake");
        }
        if (catering.isChecked()) {
            selectedCategories.add("Catering");
        }
        if (music.isChecked()) {
            selectedCategories.add("Music");
        }
        if (decor.isChecked()) {
            selectedCategories.add("Decorator");
        }
        if (floral.isChecked()) {
            selectedCategories.add("Florist");
        }

        if (selectedCategories.isEmpty()) {
            Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Vendor> multipleCat;
            try {
                multipleCat = vendorManager.getMultipleCategories(selectedCategories);

                goToVendorsList(multipleCat);
            } catch (EmptyVendorCategoryException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                selectedCategories.clear();
            }
        }
    }

    public void goToVendorsList(ArrayList<Vendor> vendors) {
        ArrayList<ParcelableVendor> parcelableVendors = new ArrayList<>();
        for (Vendor vendor : vendors) {
            parcelableVendors.add(new ParcelableVendor(vendor));
        }

        ParcelableEvent eventParcel = new ParcelableEvent(currEvent);
        Intent goToVendorsList = new Intent(this, VendorListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("vendors", parcelableVendors);
        goToVendorsList.putExtra("bundle", bundle);
        goToVendorsList.putExtra("event", eventParcel);
        startActivity(goToVendorsList);
    }

    public void close(View view) {
        finish();
    }
}
