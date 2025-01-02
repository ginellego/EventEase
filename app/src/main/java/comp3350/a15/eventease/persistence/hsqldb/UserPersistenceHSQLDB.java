package comp3350.a15.eventease.persistence.hsqldb;

import androidx.annotation.NonNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import comp3350.a15.eventease.objects.User;
import comp3350.a15.eventease.persistence.IUserPersistence;

@Singleton
public class UserPersistenceHSQLDB implements IUserPersistence {

    private final String dbPath;

    @Inject
    public UserPersistenceHSQLDB(@Named("dbPathName") String dbPathName) {
        dbPath = dbPathName;
    }

    @Override
    public boolean addNewUser(User newUser) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO USERS (isVendor, username, password) VALUES (?, ?, ?)")) {

            stmt.setBoolean(1, newUser.isVendor());
            stmt.setString(2, newUser.getUsername());
            stmt.setString(3, newUser.getPassword());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Override
    public User getUserByUsername(String username) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USERS WHERE username = ?")) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fetchFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return null;
    }

    @NonNull
    private static User fetchFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("username"), rs.getString("password"), rs.getBoolean("isVendor"));
    }

    // New method to get user ID by username
    @Override
    public int getUserIdByUsername(String username) {
        int userId = -1; // Using -1 to indicate "not found"
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM USERS WHERE username = ?")) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return userId;
    }

    @Override
    public boolean deleteUser(String userName) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM USERS WHERE username = ?")) {

            stmt.setString(1, userName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
