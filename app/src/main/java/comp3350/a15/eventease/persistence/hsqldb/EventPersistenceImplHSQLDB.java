package comp3350.a15.eventease.persistence.hsqldb;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.factory.IEventFactory;
import comp3350.a15.eventease.persistence.IEventPersistence;

@Singleton
public class EventPersistenceImplHSQLDB implements IEventPersistence {

    private final String dbPath;
    private final IEventFactory eventFactory;

    @Inject
    public EventPersistenceImplHSQLDB(@Named("dbPathName") String dbPathName, IEventFactory eventFactory) {
        dbPath = dbPathName;
        this.eventFactory = eventFactory;
    }

    @Override
    public List<Event> getAllEvents(int plannerId) {
        List<Event> events = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EVENTS where plannerId = ?")) {
            stmt.setInt(1, plannerId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = fromResultSet(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return events;
    }

    @Override
    public boolean addNewEvent(Event newEvent) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement
                     ("INSERT INTO Events (id, plannerId, eventName, eventDescription, eventLocation,eventDate, eventTime, createdTimestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, newEvent.getId());
            stmt.setInt(2, newEvent.getPlannerId());
            stmt.setString(3, newEvent.getEventName());
            stmt.setString(4, newEvent.getEventDescription());
            stmt.setString(5, newEvent.getEventLocation());
            stmt.setString(6, newEvent.getEventDate().toString());
            stmt.setString(7, newEvent.getEventTime().toString());
            stmt.setTimestamp(8, newEvent.getWhenCreated());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean doesEventExist(Event event) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM EVENTS WHERE id = ?")) {
            stmt.setInt(1, event.getId());

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean deleteEvent(Event event) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Events WHERE id = ?")) {
            stmt.setInt(1, event.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Event getEvent(int id, int plannerId) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Events WHERE id = ? and plannerId = ?")) {
            stmt.setInt(1, id);
            stmt.setInt(2, plannerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                } else {
                    throw new PersistenceException("Event could not be found.");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void saveEdit(Event eventToEdit, Event eventPostEdit) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("UPDATE EVENTS SET id = ?, plannerId = ?, eventName = ?, eventDescription = ?, eventLocation = ?, eventDate = ?, eventTime = ? WHERE id = ?")) {

            // Set parameters
            stmt.setInt(1, eventPostEdit.getId());
            stmt.setInt(2, eventPostEdit.getPlannerId());
            stmt.setString(3, eventPostEdit.getEventName());
            stmt.setString(4, eventPostEdit.getEventDescription());
            stmt.setString(5, eventPostEdit.getEventLocation());
            stmt.setString(6, eventPostEdit.getEventDate().toString());
            stmt.setString(7, eventPostEdit.getEventTime().toString());
            stmt.setInt(8, eventToEdit.getId());

            // Execute the update
            stmt.executeUpdate();

        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) {
                // This SQLState indicates a foreign key violation
                // Handle the foreign key violation error here
                // For example, you can inform the user that the eventId cannot be updated
                throw new PersistenceException("Cannot modify this event because a service request has been sent out for it.", e);
            } else {
                throw new PersistenceException(e);
            }
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    private Event fromResultSet(ResultSet rs) throws SQLException {
        int plannerId = rs.getInt("plannerId");
        String eventName = rs.getString("eventName");
        String eventDescription = rs.getString("eventDescription");
        String eventLocation = rs.getString("eventLocation");
        LocalDate eventDate = LocalDate.parse(rs.getString("eventDate"));
        LocalTime eventTime = LocalTime.parse(rs.getString("eventTime"));
        Timestamp whenCreated = rs.getTimestamp("createdTimestamp");

        return eventFactory.create(plannerId, eventName, eventDescription, eventLocation, eventDate, eventTime, whenCreated);
    }
}
