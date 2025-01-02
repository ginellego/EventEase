package comp3350.a15.eventease.objects.factory;

import comp3350.a15.eventease.objects.Invoice;


public interface IInvoiceFactory {
    Invoice create(int requestId, int plannerId,
                   String serviceType, String eventName,
                   String eventDate, String eventTime,
                   String eventLocation, long offerAccepted);
}
