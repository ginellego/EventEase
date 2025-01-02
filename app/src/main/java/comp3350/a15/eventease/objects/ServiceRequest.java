package comp3350.a15.eventease.objects;

import java.time.LocalDate;
import java.util.Objects;


public class ServiceRequest {
    private final int id;
    private final Event associatedEvent;
    private final int plannerId;
    private final int vendorID;
    private final String serviceType;
    private final LocalDate deadline;
    private final long budget;
    private ServiceStatus serviceStatus;

    public ServiceRequest(Event associatedEvent, int vendorID,
                          String serviceType, LocalDate deadline, long budget,
                          ServiceStatus serviceStatus) {
        this.associatedEvent = associatedEvent;
        plannerId = associatedEvent.getPlannerId();
        this.vendorID = vendorID;
        this.serviceType = serviceType;
        this.deadline = deadline;
        this.budget = budget;

        if (serviceStatus == null) {
            this.serviceStatus = ServiceStatus.NEW;
        } else {
            this.serviceStatus = serviceStatus;
        }

        id = hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(associatedEvent, vendorID, serviceType, deadline);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return id == ((ServiceRequest) obj).getId();
    }

    public int getId() {
        return id;
    }

    public int getPlannerId() {
        return plannerId;
    }

    public Event getAssociatedEvent() {
        return associatedEvent;
    }

    public String getServiceType() {
        return serviceType;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public long getBudget() {
        return budget;
    }

    public int getVendorID() {
        return vendorID;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public enum ServiceStatus {
        NEW,
        PENDING,
        ACCEPTED,
        REJECTED,
    }
}
