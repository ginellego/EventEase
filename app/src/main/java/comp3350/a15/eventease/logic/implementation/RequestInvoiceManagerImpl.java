package comp3350.a15.eventease.logic.implementation;

import javax.inject.Inject;
import javax.inject.Singleton;

import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IInvoiceFactory;
import comp3350.a15.eventease.persistence.IRequestInvoicePersistence;

@Singleton
public class RequestInvoiceManagerImpl implements IRequestInvoiceManager {
    private final IInvoiceFactory invoiceFactory;
    private final IRequestInvoicePersistence invoicePersistence;

    @Inject
    public RequestInvoiceManagerImpl(IRequestInvoicePersistence invoicePersistence, IInvoiceFactory invoiceFactory) {
        this.invoicePersistence = invoicePersistence;
        this.invoiceFactory = invoiceFactory;
    }

    @Override
    public int generateInvoice(ServiceRequest request) {
        Event associatedEvent = request.getAssociatedEvent();
        Invoice newInvoice = invoiceFactory.create(request.getId(), associatedEvent.getPlannerId(),
                request.getServiceType(), associatedEvent.getEventName(), associatedEvent.getEventDate().toString(),
                associatedEvent.getEventTime().toString(), associatedEvent.getEventLocation(), request.getBudget());

        storeInvoice(newInvoice);
        return newInvoice.getId();
    }

    private void storeInvoice(Invoice newInvoice) {
        invoicePersistence.addInvoice(newInvoice);
    }

    @Override
    public Invoice getInvoice(int invoiceId) {
        return invoicePersistence.getInvoice(invoiceId);
    }

    @Override
    public Invoice getInvoiceByRequestId( int requestId) { return invoicePersistence.getInvoiceByRequestId(requestId) ; }
}
