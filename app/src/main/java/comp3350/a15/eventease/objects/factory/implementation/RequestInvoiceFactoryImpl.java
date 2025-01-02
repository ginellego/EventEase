package comp3350.a15.eventease.objects.factory.implementation;

import javax.inject.Inject;

import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.factory.IInvoiceFactory;

public class RequestInvoiceFactoryImpl implements IInvoiceFactory {
    @Inject
    public RequestInvoiceFactoryImpl() {
    }

    @Override
    public Invoice create(int requestId, int plannerId,
                          String serviceType, String eventName,
                          String eventDate, String eventTime,
                          String eventLocation, long offerAccepted) {
        return new Invoice(requestId, plannerId, serviceType, eventName, eventDate, eventTime, eventLocation, offerAccepted);
    }

}
