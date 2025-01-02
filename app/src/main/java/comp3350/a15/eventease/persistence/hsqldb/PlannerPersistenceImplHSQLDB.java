package comp3350.a15.eventease.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;

import comp3350.a15.eventease.objects.Planner;
import comp3350.a15.eventease.objects.factory.IPlannerFactory;
import comp3350.a15.eventease.persistence.IPlannerPersistence;

public class PlannerPersistenceImplHSQLDB implements IPlannerPersistence {
    private final String dbPath;
    private final IPlannerFactory plannerFactory;

    @Inject
    public PlannerPersistenceImplHSQLDB(@Named("dbPathName") String dbPathName, IPlannerFactory plannerFactory) {
        this.dbPath = dbPathName;
        this.plannerFactory = plannerFactory;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Override
    public Planner getPlannerByUserId(int userId) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PLANNER WHERE ACCOUNTID = ?")) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                } else {
                    throw new PersistenceException("Planner could not be found.");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Planner fromResultSet(ResultSet rs) {
        try {
            int plannerId = rs.getInt("plannerId");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            String phone = rs.getString("phonenumber");
            String email = rs.getString("email");
            float rating = rs.getFloat("rating");
            String bio = rs.getString("bio");
            return plannerFactory.create(plannerId, firstname, lastname, phone, email, rating, bio);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean addNewPlanner(Planner planner) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO PLANNER (plannerID, accountid, firstname, lastname, phonenumber, email, rating, bio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setInt(1, planner.getId());
            stmt.setInt(2, planner.getAccountId());
            stmt.setString(3, planner.getFirstname());
            stmt.setString(4, planner.getLastname());
            stmt.setString(5, planner.getPhoneNumber());
            stmt.setString(6, planner.getEmail());
            stmt.setFloat(7, planner.getRating());
            stmt.setString(8, planner.getBio());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean doesPlannerExist(Planner planner) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM PLANNER WHERE PLANNERID = ?")) {
            stmt.setInt(1, planner.getId());

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void saveEditPlanner(Planner plannerToEdit, Planner plannerPostEdit) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("UPDATE PLANNER SET firstname = ?, lastname = ?, PHONENUMBER = ?, email = ?, rating = ?, bio = ? WHERE PLANNERID = ?")) {

            // Set parameters
            stmt.setString(1, plannerPostEdit.getFirstname());
            stmt.setString(2, plannerPostEdit.getLastname());
            stmt.setString(3, plannerPostEdit.getPhoneNumber());
            stmt.setString(4, plannerPostEdit.getEmail());
            stmt.setFloat(5, plannerPostEdit.getRating());
            stmt.setString(6, plannerPostEdit.getBio());
            stmt.setInt(7, plannerToEdit.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) {
                // This SQLState indicates a foreign key violation
                // Handle the foreign key violation error here
                // For example, you can inform the user that the eventId cannot be updated
                throw new PersistenceException("Cannot modify this planner.", e);
            } else {
                throw new PersistenceException(e);
            }
        }
    }
}
