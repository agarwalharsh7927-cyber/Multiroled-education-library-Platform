import java.sql.*;

public class TransactionDAO {
    private Connection conn;
    public TransactionDAO() { this.conn = DatabaseConnection.getConnection(); }

    public void addTransaction(TransactionRecord t, String libraryId) {
        String sql = "INSERT INTO transactions (transaction_id, username, book_title, activity_type, date, details, amount, library_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getTransactionId());
            pstmt.setString(2, t.getStudent() != null ? t.getStudent().getUsername() : "ADMIN");
            pstmt.setString(3, t.getBook() != null ? t.getBook().getTitle() : "N/A");
            pstmt.setString(4, t.getType().name());
            pstmt.setDate(5, Date.valueOf(t.getDate()));
            pstmt.setString(6, t.getDetails());
            pstmt.setDouble(7, t.getAmount());
            pstmt.setString(8, libraryId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}