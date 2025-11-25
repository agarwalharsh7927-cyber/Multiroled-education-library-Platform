import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    public void addUser(User user, String libraryId) {
        String sql = "INSERT INTO users (username, password, full_name, gender, aadhaar_number, phone_number, address, library_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getGender());
            pstmt.setString(5, user.getAadhaarNumber());
            pstmt.setString(6, user.getPhoneNumber());
            pstmt.setString(7, user.getAddress());
            pstmt.setString(8, libraryId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User u = new User(
                    rs.getString("username"), rs.getString("password"), rs.getString("full_name"),
                    rs.getString("gender"), rs.getString("aadhaar_number"), rs.getString("phone_number"),
                    rs.getString("address")
                );
                if (rs.getBoolean("has_paid_fees")) u.payFees();
                u.addExpenditure(rs.getDouble("elibrary_expenditure"));
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET password=?, has_paid_fees=?, elibrary_expenditure=? WHERE username=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getPassword());
            pstmt.setBoolean(2, user.hasPaidFees());
            pstmt.setDouble(3, user.getELibraryExpenditure());
            pstmt.setString(4, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- ADDED MISSING METHODS BELOW ---

    public List<User> getUsersByLibrary(String libraryId) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE library_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libraryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User u = new User(
                    rs.getString("username"), rs.getString("password"), rs.getString("full_name"),
                    rs.getString("gender"), rs.getString("aadhaar_number"), rs.getString("phone_number"),
                    rs.getString("address")
                );
                if(rs.getBoolean("has_paid_fees")) u.payFees();
                u.addExpenditure(rs.getDouble("elibrary_expenditure"));
                list.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void updateUserFees(String username, boolean status, double expenditure) {
        String sql = "UPDATE users SET has_paid_fees = ?, elibrary_expenditure = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, status);
            pstmt.setDouble(2, expenditure);
            pstmt.setString(3, username);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}