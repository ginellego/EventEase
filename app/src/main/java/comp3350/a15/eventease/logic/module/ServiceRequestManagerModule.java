package comp3350.a15.eventease.logic.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.application.qualifier.AcceptedServiceRequest;
import comp3350.a15.eventease.application.qualifier.RejectedServiceRequest;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.logic.IServiceRequestManager;
import comp3350.a15.eventease.logic.implementation.AcceptedServiceRequestManager;
import comp3350.a15.eventease.logic.implementation.RejectedServiceRequestManager;
import comp3350.a15.eventease.logic.implementation.UnresolvedServiceRequestManager;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ServiceRequestManagerModule {
    @Binds
    @Singleton
    @UnresolvedServiceRequest
    abstract IServiceRequestManager provideUnresolvedServiceRequestManager(UnresolvedServiceRequestManager serviceRequestManager);

    @Binds
    @Singleton
    @AcceptedServiceRequest
    abstract IServiceRequestManager provideAcceptedServiceRequestManager(AcceptedServiceRequestManager serviceRequestManager);

    @Binds
    @Singleton
    @RejectedServiceRequest
    abstract IServiceRequestManager provideRejectedServiceRequestManager(RejectedServiceRequestManager serviceRequestManager);
}
