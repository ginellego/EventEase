package comp3350.a15.eventease.persistence;

import comp3350.a15.eventease.objects.Invoice;

public interface IRequestInvoicePersistence {
    void addInvoice(Invoice invoice);

    Invoice getInvoice(int invoiceId);

    Invoice getInvoiceByRequestId(int requestId) ;
}
