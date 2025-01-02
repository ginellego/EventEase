package comp3350.a15.eventease.logic.implementation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import comp3350.a15.eventease.application.qualifier.RejectedServiceRequest;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;

@Singleton
public class RejectedServiceRequestManager extends BaseServiceRequestManager {
    @Inject
    public RejectedServiceRequestManager(@RejectedServiceRequest IServiceRequestPersistence serviceRequestPersistence,
                                         IServiceRequestFactory serviceRequestFactory) {
        super(serviceRequestPersistence, serviceRequestFactory);
    }

    @Override
    public List<ServiceRequest> getAllRequests(int vendorId) {
        return serviceRequestPersistence.getAllRequests(vendorId);
    }
}
