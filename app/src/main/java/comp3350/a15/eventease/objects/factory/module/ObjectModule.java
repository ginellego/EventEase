package comp3350.a15.eventease.objects.factory.module;

import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.objects.factory.IInvoiceFactory;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.objects.factory.implementation.EventFactoryImpl;
import comp3350.a15.eventease.objects.factory.implementation.RequestInvoiceFactoryImpl;
import comp3350.a15.eventease.objects.factory.implementation.ServiceRequestFactoryImpl;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ObjectModule {
    @Binds
    abstract IServiceRequestFactory ProvideServiceRequest(ServiceRequestFactoryImpl serviceRequestFactory);

    @Binds
    abstract IEventFactory ProvideEvents(EventFactoryImpl eventFactory);
    @Binds
    abstract IInvoiceFactory ProvideInvoice(RequestInvoiceFactoryImpl invoiceFactory);
}
