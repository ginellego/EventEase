package comp3350.a15.eventease.logic.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.logic.IRequestInvoiceManager;
import comp3350.a15.eventease.logic.implementation.RequestInvoiceManagerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RequestInvoiceManagerModule {
    @Binds
    @Singleton
    abstract IRequestInvoiceManager provideInvoiceManager(RequestInvoiceManagerImpl invoiceManager);
}
