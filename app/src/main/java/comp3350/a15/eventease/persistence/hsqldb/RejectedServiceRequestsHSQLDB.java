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

public class RejectedServiceRequestsHSQLDB extends ServiceRequestPersistenceHSQLDB {
    @Inject
    public RejectedServiceRequestsHSQLDB(@Named("dbPathName") String DBPathName, IServiceRequestFactory serviceRequestFactory,
                                         IEventPersistence eventPersistence) {
        super(DBPathName, serviceRequestFactory, eventPersistence);
    }

    @Override
    public List<ServiceRequest> getAllRequests(int vendorId) {
        List<ServiceRequest> result = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM SERVICE_REQUESTS where vendorId = ? AND serviceStatus = ?")) {
            stmt.setInt(1, vendorId);
            stmt.setString(2, ServiceRequest.ServiceStatus.REJECTED.name());

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
