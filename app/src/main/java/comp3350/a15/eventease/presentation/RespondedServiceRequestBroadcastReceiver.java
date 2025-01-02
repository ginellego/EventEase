package comp3350.a15.eventease.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@Singleton
public class RespondedServiceRequestBroadcastReceiver extends BroadcastReceiver {
    public static final String ACCEPTED_REQUESTS = "accepted_requests";
    public static final String REJECTED_REQUESTS = "rejected_requests";

    @Inject
    RespondedServiceRequestBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if ("acceptedServiceRequest".equals(intent.getAction())) {
                handleAcceptedServiceRequest(context, intent);
            } else if ("rejectedServiceRequest".equals(intent.getAction())) {
                handleRejectedServiceRequest(context, intent);
            }
        }
    }

    private void handleAcceptedServiceRequest(Context context, Intent intent) {
        int invoiceId = intent.getIntExtra("invoice_id", -1);
        storeAcceptedServiceRequest(context, invoiceId);
    }

    private void handleRejectedServiceRequest(Context context, Intent intent) {
        ParcelableServiceRequest requestParcel = intent.getParcelableExtra("request", ParcelableServiceRequest.class);
        if (requestParcel != null) {
            storeRejectedServiceRequest(context, requestParcel);
        }
    }

    private void storeAcceptedServiceRequest(Context context, int invoiceId) {
        // Store in Shared Preferences
        SharedPreferences prefs = context.getSharedPreferences(ACCEPTED_REQUESTS, Context.MODE_PRIVATE);
        prefs.edit().putInt(invoiceId + "_invoice_id", invoiceId).apply();
    }

    private static void storeRejectedServiceRequest(Context context, ParcelableServiceRequest request) {
        // Generate a unique key
        long timestamp = System.currentTimeMillis();
        String key = request.getId() + "_" + timestamp;

        // Store in Shared Preferences
        SharedPreferences prefs = context.getSharedPreferences(REJECTED_REQUESTS, Context.MODE_PRIVATE);
        prefs.edit().putString(key, serializeRequest(request)).apply();
    }

    private static String serializeRequest(ParcelableServiceRequest request) {
        Gson gson = new Gson();
        return gson.toJson(request);
    }
}


