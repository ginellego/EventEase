package comp3350.a15.eventease.persistence.hsqldb;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import comp3350.a15.eventease.objects.Invoice;
import comp3350.a15.eventease.objects.factory.IInvoiceFactory;
import comp3350.a15.eventease.persistence.IRequestInvoicePersistence;

@Singleton
public class InvoicePersistenceImplHSQLDB implements IRequestInvoicePersistence {

    private final String dbPath;
    private final IInvoiceFactory invoiceFactory;

    @Inject
    public InvoicePersistenceImplHSQLDB(@Named("dbPathName") String dbPathName, IInvoiceFactory invoiceFactory) {
        dbPath = dbPathName;
        this.invoiceFactory = invoiceFactory;
    }

    @Override
    public void addInvoice(Invoice invoice) {
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO INVOICES (id, request_id, planner_id, service_type, event_name, event_date, event_time, event_location, offer_accepted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, invoice.getId());
            stmt.setInt(2, invoice.getRequestId());
            stmt.setInt(3, invoice.getPlannerId());
            stmt.setString(4, invoice.getServiceType());
            stmt.setString(5, invoice.getEventName());
            stmt.setString(6, invoice.getEventDate());
            stmt.setString(7, invoice.getEventTime());
            stmt.setString(8, invoice.getEventLocation());
            stmt.setLong(9, invoice.getOfferAccepted());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Override
    public Invoice getInvoice(int invoiceId) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM INVOICES WHERE id = ?")) {
            statement.setInt(1, invoiceId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return fromResultSet(resultSet);
            } else {
                throw new PersistenceException("invoice could not be found");
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Invoice getInvoiceByRequestId(int requestId) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM INVOICES WHERE request_id = ?")) {
            statement.setInt(1, requestId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return fromResultSet(resultSet);
            } else {
                throw new PersistenceException("Invoice for request ID " + requestId + " could not be found");
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }


    private Invoice fromResultSet(ResultSet rs) throws SQLException {
        int requestId = rs.getInt("request_id");
        int plannerId = rs.getInt("planner_id");
        String serviceType = rs.getString("service_type");
        String eventName = rs.getString("event_name");
        String eventDate = rs.getString("event_date");
        String eventTime = rs.getString("event_time");
        String eventLocation = rs.getString("event_location");
        long offerAccepted = rs.getLong("offer_accepted");

        return invoiceFactory.create(requestId, plannerId, serviceType, eventName, eventDate, eventTime, eventLocation, offerAccepted);
    }
}
