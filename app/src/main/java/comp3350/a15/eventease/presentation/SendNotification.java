package comp3350.a15.eventease.presentation;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Optional;

import comp3350.a15.eventease.objects.ServiceRequest;

class SendNotification {

    public static final String VENDOR_ID_KEY = "vendor_id";
    public static final String INVOICE_ID_KEY = "invoice_id";
    public static final String REQUEST_KEY = "request";

    static void sendNotificationToPlanner(Context context, Optional<Integer> invoiceId, ServiceRequest request,
                                          String acceptedOrRejected) {
        //Send notification to planner about accepted request
        Intent intent = new Intent(acceptedOrRejected);
        invoiceId.ifPresentOrElse(invId -> intent.putExtra(INVOICE_ID_KEY, invId),
                () -> intent.putExtra(REQUEST_KEY, new ParcelableServiceRequest(request)));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    static void sendNotificationToVendor(Context context, int vendorId) {
        //Send notification to vendor about new requests
        Intent intent = new Intent("newServiceRequest");
        intent.putExtra(VENDOR_ID_KEY, vendorId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
