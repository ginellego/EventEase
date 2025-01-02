package comp3350.a15.eventease.persistence.hsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import comp3350.a15.eventease.objects.ServiceRequest;
import comp3350.a15.eventease.objects.factory.IServiceRequestFactory;
import comp3350.a15.eventease.persistence.IEventPersistence;

@Singleton
public class UnresolvedServiceRequestsHSQLDB extends ServiceRequestPersistenceHSQLDB {
    @Inject
    public UnresolvedServiceRequestsHSQLDB(@Named("dbPathName") String DBPathName, IServiceRequestFactory serviceRequestFactory,
                                           IEventPersistence eventPersistence) {
        super(DBPathName, serviceRequestFactory, eventPersistence);
    }

    @Override
    public List<ServiceRequest> getAllRequests(int vendorId) {
        List<ServiceRequest> result = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM SERVICE_REQUESTS where vendorId = ? AND serviceStatus IN (?, ?)")) {
            stmt.setInt(1, vendorId);
            stmt.setString(2, ServiceRequest.ServiceStatus.NEW.name());
            stmt.setString(3, ServiceRequest.ServiceStatus.PENDING.name());

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
}
