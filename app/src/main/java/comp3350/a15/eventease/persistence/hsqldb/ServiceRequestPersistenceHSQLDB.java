package comp3350.a15.eventease.persistence.hsqldb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.IServiceRequestPersistence;

@Singleton
public abstract class ServiceRequestPersistenceHSQLDB implements IServiceRequestPersistence {
    private final String dbPath;
    private final IServiceRequestFactory serviceRequestFactory;
    private final IEventPersistence eventPersistence;

    public ServiceRequestPersistenceHSQLDB(@Named("dbPathName") String DBPathName, IServiceRequestFactory serviceRequestFactory,
                                           IEventPersistence eventPersistence) {
        dbPath = DBPathName;
        this.serviceRequestFactory = serviceRequestFactory;
        this.eventPersistence = eventPersistence;
    }

    @Override
    abstract public List<ServiceRequest> getAllRequests(int vendorId);

    @Override
    public void addNewRequest(ServiceRequest newRequest) {
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO SERVICE_REQUESTS (id, associatedEventId, plannerId, vendorId, serviceType, deadline, budget, serviceStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, newRequest.getId());
            stmt.setInt(2, newRequest.getAssociatedEvent().getId());
            stmt.setInt(3, newRequest.getAssociatedEvent().getPlannerId());
            stmt.setInt(4, newRequest.getVendorID());
            stmt.setString(5, newRequest.getServiceType());
            stmt.setDate(6, Date.valueOf(newRequest.getDeadline().toString()));
            stmt.setLong(7, newRequest.getBudget());
            stmt.setString(8, newRequest.getServiceStatus().name()); // serviceStatus is an enum

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    protected Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Override
    public void deleteRequest(ServiceRequest request) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM SERVICE_REQUESTS WHERE id = ?")) {
            statement.setInt(1, request.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean doesRequestExist(ServiceRequest request) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM SERVICE_REQUESTS WHERE id = ?")) {
            stmt.setInt(1, request.getId());

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public ServiceRequest getRequest(int id, int vendorId) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM SERVICE_REQUESTS WHERE id = ? and vendorId = ?")) {
            statement.setInt(1, id);
            statement.setInt(2, vendorId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return fromResultSet(resultSet);
            } else {
                throw new PersistenceException("Request could not be found");
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<ServiceRequest> getAllRequestsOfEvent(int eventId) {
        List<ServiceRequest> result = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM SERVICE_REQUESTS where ASSOCIATEDEVENTID = ?")) {
            stmt.setInt(1, eventId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                ServiceRequest request = fromResultSet(resultSet);
                result.add(request);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }

    @Override
    public void setServiceStatus(int id, ServiceRequest.ServiceStatus serviceStatus) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("UPDATE SERVICE_REQUESTS SET serviceStatus = ? WHERE id = ?")) {

            // Set parameters
            stmt.setString(1, serviceStatus.name());
            stmt.setInt(2, id);

            // Execute the update
            int result = stmt.executeUpdate();
            System.out.println(result > 0);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public ServiceRequest fromResultSet(ResultSet rs) throws SQLException {
        // Extract data from ResultSet and create ServiceRequest object
        int plannerId = rs.getInt("plannerId");
        Event associatedEvent = fetchEvent(rs.getInt("associatedEventId"), plannerId); // Assuming you have a method to create Event object from ResultSet
        int vendorID = rs.getInt("vendorId");
        String serviceType = rs.getString("serviceType");
        LocalDate deadline = LocalDate.parse(rs.getDate("deadline").toString());
        long budget = rs.getLong("budget");
        String serviceStatus = rs.getString("serviceStatus");

        return serviceRequestFactory.create(associatedEvent, vendorID, serviceType, deadline, budget,
                ServiceRequest.ServiceStatus.valueOf(serviceStatus));
    }

    private Event fetchEvent(int associatedEventId, int plannerId) {
        return eventPersistence.getEvent(associatedEventId, plannerId);
    }
}
