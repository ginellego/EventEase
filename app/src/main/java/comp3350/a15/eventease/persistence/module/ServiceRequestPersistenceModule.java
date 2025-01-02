package comp3350.a15.eventease.persistence.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.application.qualifier.AcceptedServiceRequest;
import comp3350.a15.eventease.application.qualifier.RejectedServiceRequest;
import comp3350.a15.eventease.application.qualifier.UnresolvedServiceRequest;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;
import comp3350.a15.eventease.persistence.hsqldb.AcceptedServiceRequestsHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.RejectedServiceRequestsHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.UnresolvedServiceRequestsHSQLDB;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ServiceRequestPersistenceModule {
    @Binds
    @Singleton
    @AcceptedServiceRequest
    public abstract IServiceRequestPersistence provideAcceptedServiceRequestPersistence(AcceptedServiceRequestsHSQLDB acceptedServiceRequests);

    @Binds
    @Singleton
    @RejectedServiceRequest
    public abstract IServiceRequestPersistence provideRejectedServiceRequestPersistence(RejectedServiceRequestsHSQLDB rejectedServiceRequests);

    @Binds
    @Singleton
    @UnresolvedServiceRequest
    public abstract IServiceRequestPersistence provideUnresolvedServiceRequestPersistence(UnresolvedServiceRequestsHSQLDB unresolvedServiceRequests);
}
