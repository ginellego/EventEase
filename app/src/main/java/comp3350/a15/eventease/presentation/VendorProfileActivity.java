package comp3350.a15.eventease.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.objects.Vendor;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorProfileActivity extends AppCompatActivity {

    @Inject
    IVendorManager vendorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        ParcelableVendor parcel;
        parcel = getIntent().getParcelableExtra("vendor", ParcelableVendor.class);
        Vendor currVendor = parcel.toVendor(vendorManager);

        TextView vendorName = findViewById(R.id.vendorName);
        TextView vendorDescription = findViewById(R.id.vendorDescription);
        TextView vendorRating = findViewById(R.id.vendorRating);
        TextView vendorPhone = findViewById(R.id.vendorPhone);
        TextView vendorCost = findViewById(R.id.vendorCost);
        TextView vendorEmail = findViewById(R.id.vendorEmail);
        ImageView vendorPicture = findViewById(R.id.vendorPicture);
        TextView vendorServiceType = findViewById(R.id.vendorServiceType);

        if (currVendor != null) {
            vendorName.setText(currVendor.getVendorName());
            vendorDescription.setText(currVendor.getVendorDescription());
            vendorRating.setText(currVendor.getRating());
            vendorPhone.setText(currVendor.getVendorNumber());
            vendorCost.setText(String.valueOf(currVendor.getCost()));
            vendorEmail.setText(currVendor.getVendorEmail());
            vendorPicture.setImageResource(currVendor.getVendorPictures());
            vendorServiceType.setText(currVendor.getServiceType());
        }
    }

    public void goBack(View view) {
        finish();
    }
}