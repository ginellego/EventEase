package comp3350.a15.eventease.logic.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.logic.IVendorManager;
import comp3350.a15.eventease.logic.implementation.VendorManagerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class VendorManagerModule {
    @Binds
    @Singleton
    abstract IVendorManager provideVendorManager(VendorManagerImpl vendorManager);
}
