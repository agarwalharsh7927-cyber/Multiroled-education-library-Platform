import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAO {
    private Connection conn;

    public LibraryDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    public void registerLibrary(LibraryProfile p) {
        String sql = "INSERT INTO libraries (library_id, name, address, location_link, photo_path, owner_name, owner_phone, owner_email, owner_aadhaar, is_approved, payment_done) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getLibraryId());
            ps.setString(2, p.getLibraryName());
            ps.setString(3, p.getAddress());
            ps.setString(4, p.getLocationLink());
            ps.setString(5, p.getPhotoPath());
            ps.setString(6, p.getOwnerName());
            ps.setString(7, p.getOwnerPhone());
            ps.setString(8, p.getOwnerEmail());
            ps.setString(9, p.getOwnerAadhaar());
            ps.setBoolean(10, p.isApproved());
            ps.setBoolean(11, p.isPaymentDone());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<LibraryProfile> getAllLibraries() {
        List<LibraryProfile> list = new ArrayList<>();
        String sql = "SELECT * FROM libraries";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LibraryProfile p = new LibraryProfile(
                    rs.getString("name"), rs.getString("address"), rs.getString("location_link"),
                    rs.getString("photo_path"), rs.getString("owner_name"), rs.getString("owner_phone"),
                    rs.getString("owner_email"), rs.getString("owner_aadhaar")
                );
                p.setApproved(rs.getBoolean("is_approved"));
                p.setPaymentDone(rs.getBoolean("payment_done"));
                // In a real app, we would sync the ID from DB, but for now we assume profiles match
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void approveLibrary(String libId, String adminUser, String adminPass) {
        String sql = "UPDATE libraries SET is_approved = TRUE, admin_username = ?, admin_password = ? WHERE library_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adminUser);
            ps.setString(2, adminPass);
            ps.setString(3, libId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean authenticateLibrarian(String username, String password) {
        String sql = "SELECT * FROM libraries WHERE admin_username = ? AND admin_password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}