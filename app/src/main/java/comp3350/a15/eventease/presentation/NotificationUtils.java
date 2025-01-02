package comp3350.a15.eventease.presentation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.Arrays;
import java.util.Optional;

import javax.inject.Inject;

import comp3350.a15.eventease.R;
import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.ServiceRequest;

public class NotificationUtils {
    private static final String GROUP_KEY = "EVENT_EASE_GROUP_KEY";
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "channel_name";
    private static NotificationManager notificationManager;

    private final IEventManager eventManager;

    @Inject
    public NotificationUtils(IEventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void showRespondedRequestNotification(Context context, String title, String message, Optional<Invoice> invoice,
                                                 Optional<ServiceRequest> request, boolean accepted) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        // Create a notification channel (required for Android 8.0 and above)
        NotificationChannel channel = getNotificationChannel();
        notificationManager.createNotificationChannel(channel);

        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.request_accepted_notification_icon)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY);

        Invoice requestInvoice = invoice.orElse(null);
        ServiceRequest rejectedRequest = request.orElse(null);
        if (accepted && requestInvoice != null) {
            buildAcceptedRequestNotification(context, requestInvoice, builder);
            // Show the notification
            notificationManager.notify(/* Unique notification ID */ requestInvoice.getRequestId(), builder.build());
        } else if (rejectedRequest != null) {
            buildRejectedRequestNotification(context, rejectedRequest, builder);
            // Show the notification
            notificationManager.notify(/* Unique notification ID */ rejectedRequest.getId(), builder.build());
        }

        // Summary notification to group multiple notifications
        updateSummaryNotification(notificationManager, context);
    }

    public void showNewRequestNotification(Context context, int numNewRequests, int userID) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        // Create a notification channel (required for Android 8.0 and above)
        NotificationChannel channel = getNotificationChannel();
        notificationManager.createNotificationChannel(channel);

        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("You have new " + numNewRequests + " requests.")
                .setContentText("Click to show all pending requests")
                .setSmallIcon(R.drawable.event_ease_notification_icon)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY);

        buildNewRequestNotification(context, builder, numNewRequests, userID);

        // Show the notification
        notificationManager.notify(/* Unique notification ID */numNewRequests, builder.build());
    }

    private void buildNewRequestNotification(Context context, NotificationCompat.Builder builder,
                                             int numRequests, int userID) {
        Intent intent = new Intent(context, VendorMainActivity.class);
        intent.putExtra("userId", userID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Create a PendingIntent that opens the List of unresolved service requests
        // Activity when the notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(context, numRequests, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setContentIntent(pendingIntent);
    }

    @NonNull
    private static NotificationChannel getNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setDescription("Channel Description");
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        channel.canShowBadge();
        channel.setAllowBubbles(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        return channel;
    }

    private void buildAcceptedRequestNotification(Context context, Invoice requestInvoice, NotificationCompat.Builder builder) {
        ParcelableInvoice invParcel = new ParcelableInvoice(requestInvoice);
        Intent intent = new Intent(context, PlannerMainActivity.class);
        intent.putExtra("invoice", invParcel); // Pass invoice details as an extra
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Check if there's a corresponding rejected request notification
        if (DoesRequestNotificationExist(requestInvoice.getRequestId())) {
            // Cancel the rejected request notification
            cancelRequestNotification(requestInvoice.getRequestId());
        }

        // Create a PendingIntent that opens the MainActivity when the notification is clicked
        // in order to display the invoice as a dialog fragment
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestInvoice.getRequestId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setSmallIcon(R.drawable.request_accepted_notification_icon)
                .setContentIntent(pendingIntent);
    }

    private void buildRejectedRequestNotification(Context context, ServiceRequest rejectedRequest, NotificationCompat.Builder builder) {
        Event associatedEvent = rejectedRequest.getAssociatedEvent();
        Intent intent = new Intent(context, PlannerMainActivity.class);
        ParcelableEvent eventParcel =
                new ParcelableEvent(eventManager.getEvent(associatedEvent.getId(), associatedEvent.getPlannerId()));

        // Pass event as an extra to show what event had its request rejected
        intent.putExtra("event", eventParcel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Check if there's a corresponding accepted request notification
        if (DoesRequestNotificationExist(rejectedRequest.getId())) {
            // Cancel the rejected request notification
            cancelRequestNotification(rejectedRequest.getId());
        }

        // Create a PendingIntent that opens the EventDetails Activity when the notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(context, rejectedRequest.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setSmallIcon(R.drawable.request_declined_notification_icon)
                .setContentIntent(pendingIntent);
    }

    private static void updateSummaryNotification(NotificationManager manager, Context context) {
        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.event_ease_notification_icon)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setContentTitle("You have a few responses");

        manager.notify(1, summaryBuilder.build());
    }

    private static boolean DoesRequestNotificationExist(int requestId) {
        return Arrays.stream(notificationManager.getActiveNotifications())
                .anyMatch(notification -> notification.getId() == requestId);
    }

    private void cancelRequestNotification(int requestId) {
        notificationManager.cancel(requestId);
    }
}
