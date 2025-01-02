package comp3350.a15.eventease.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@Singleton
public class NewServiceRequestBroadcastReceiver extends BroadcastReceiver {
    private static final String NEW_REQUESTS = "new_requests";
    private static final String NUM_REQUESTS = "num_requests";
    public static final String VENDOR_ID_KEY = "vendor_id";

    @Inject
    public NewServiceRequestBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            int vendorId = intent.getIntExtra(VENDOR_ID_KEY, -1);
            handleNewServiceRequest(context, vendorId);
        }
    }

    private void handleNewServiceRequest(Context context, int vendorId) {
        SharedPreferences prefs = context.getSharedPreferences(NEW_REQUESTS, Context.MODE_PRIVATE);
        int numRequests = prefs.getInt(NUM_REQUESTS + "_" + vendorId, 0);
        prefs.edit()
                .putInt(NUM_REQUESTS + "_" + vendorId, ++numRequests)
                .putInt(VENDOR_ID_KEY + "_" + vendorId, vendorId).apply();
    }
}


