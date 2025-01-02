package comp3350.a15.eventease.persistence.hsqldb;

import androidx.annotation.NonNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import comp3350.a15.eventease.logic.exceptions.EmptyVendorCategoryException;
import comp3350.a15.eventease.logic.exceptions.VendorNotFoundException;
import comp3350.a15.eventease.objects.Vendor;
import comp3350.a15.eventease.persistence.IVendorPersistence;

@Singleton
public class VendorPersistenceHSQLDB implements IVendorPersistence {
    private final String dbPath;

    @Inject
    public VendorPersistenceHSQLDB(@Named("dbPathName") String dbPathName) {
        dbPath = dbPathName;
    }

    @Override
    public ArrayList<Vendor> getAllVendors() {
        ArrayList<Vendor> vendors = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM VENDORS")) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vendor vendor = fetchVendorFromResultSet(rs);
                    vendors.add(vendor);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return vendors;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @NonNull
    private static Vendor fetchVendorFromResultSet(ResultSet rs) throws SQLException {
        return new Vendor(
                rs.getInt("ACCOUNTID"),
                rs.getString("NAME"),
                rs.getString("SERVICETYPE"),
                rs.getString("DESCRIPTION"),
                rs.getString("PHONENUMBER"),
                rs.getString("EMAIL"),
                rs.getInt("COST"),
                rs.getString("RATING"),
                rs.getInt("PICTURE"),
                rs.getInt("CAPACITY")
        );
    }

    @Override
    public boolean addNewVendor(Vendor newVendor) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO VENDORS (VENDORID,ACCOUNTID,NAME,SERVICETYPE,DESCRIPTION,PHONENUMBER,EMAIL,COST,RATING,PICTURE,CAPACITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, newVendor.getVendorId());
            stmt.setInt(2, newVendor.getAccountId());
            stmt.setString(3, newVendor.getVendorName());
            stmt.setString(4, newVendor.getServiceType());
            stmt.setString(5, newVendor.getVendorDescription());
            stmt.setString(6, newVendor.getVendorNumber());
            stmt.setString(7, newVendor.getVendorEmail());
            stmt.setInt(8, newVendor.getCost());
            stmt.setString(9, newVendor.getRating());
            stmt.setInt(10, newVendor.getVendorPictures());
            stmt.setInt(11, newVendor.getCapacity());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean doesVendorExist(Vendor vendor) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM VENDORS WHERE ACCOUNTID = ?")) {

            stmt.setInt(1, vendor.getAccountId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return false;
    }

    @Override
    public Vendor getVendorById(int vendorId) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM VENDORS WHERE VENDORID = ?")) {

            stmt.setInt(1, vendorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fetchVendorFromResultSet(rs);
                } else {
                    throw new VendorNotFoundException("Vendor not found");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Vendor getVendorByAccountId(int accountId) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM VENDORS WHERE ACCOUNTID = ?")) {

            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fetchVendorFromResultSet(rs);
                } else {
                    throw new VendorNotFoundException("Vendor not found");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<Vendor> getVendorByServiceType(String serviceType) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM VENDORS WHERE SERVICETYPE = ?")) {
            stmt.setString(1, serviceType);

            ArrayList<Vendor> vendors = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    do {
                        vendors.add(fetchVendorFromResultSet(rs));
                    } while (rs.next());
                } else {
                    throw new EmptyVendorCategoryException();
                }
                return vendors;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
