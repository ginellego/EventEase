package comp3350.a15.eventease.persistence.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.persistence.IRequestInvoicePersistence;
import comp3350.a15.eventease.persistence.hsqldb.InvoicePersistenceImplHSQLDB;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class InvoicePersistenceModule {
    @Binds
    @Singleton
    public abstract IRequestInvoicePersistence provideInvoicePersistence(InvoicePersistenceImplHSQLDB invoicePersistenceImpl);
}
