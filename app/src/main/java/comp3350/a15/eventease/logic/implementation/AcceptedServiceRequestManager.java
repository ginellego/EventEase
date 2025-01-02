package comp3350.a15.eventease.logic.implementation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import comp3350.a15.eventease.application.qualifier.AcceptedServiceRequest;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;

@Singleton
public class AcceptedServiceRequestManager extends BaseServiceRequestManager {
    @Inject
    public AcceptedServiceRequestManager(@AcceptedServiceRequest IServiceRequestPersistence serviceRequestPersistence,
                                         IServiceRequestFactory serviceRequestFactory) {
        super(serviceRequestPersistence, serviceRequestFactory);
    }

    @Override
    public List<ServiceRequest> getAllRequests(int vendorId) {
        return serviceRequestPersistence.getAllRequests(vendorId);
    }
}
