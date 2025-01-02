package comp3350.a15.eventease.logic;

import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.ServiceRequest;

public interface IRequestInvoiceManager {
    int generateInvoice(ServiceRequest request);

    Invoice getInvoice(int invoiceId);

    Invoice getInvoiceByRequestId ( int requestId) ;
}
