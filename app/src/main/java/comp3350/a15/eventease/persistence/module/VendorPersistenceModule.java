package comp3350.a15.eventease.persistence.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.persistence.IVendorPersistence;
import comp3350.a15.eventease.persistence.hsqldb.VendorPersistenceHSQLDB;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class VendorPersistenceModule {
    @Binds
    @Singleton
    public abstract IVendorPersistence provideVendorPersistence(VendorPersistenceHSQLDB vendorPersistence);
}
