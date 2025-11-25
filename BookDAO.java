import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public BookDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    public void addBook(Book book, String libraryId) {
        String sql = "INSERT INTO books (title, author, category, subject, location_row, location_section, location_block, image_path, library_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setString(4, book.getSubject());
            pstmt.setString(5, book.getLocationRow());
            pstmt.setString(6, book.getLocationSection());
            pstmt.setString(7, book.getLocationBlock());
            pstmt.setString(8, book.getImagePath());
            pstmt.setString(9, libraryId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateBookStatus(Book book) {
        String sql = "UPDATE books SET is_available = ?, borrowed_by_user = ?, borrowed_date = ?, current_borrowing_id = ? WHERE title = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, book.isAvailable());
            pstmt.setString(2, book.getBorrowedBy() != null ? book.getBorrowedBy().getUsername() : null);
            pstmt.setDate(3, book.getBorrowedDate() != null ? Date.valueOf(book.getBorrowedDate()) : null);
            pstmt.setString(4, book.getCurrentBorrowingId());
            pstmt.setString(5, book.getTitle());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteBook(String title) {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM books WHERE title=?")) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Book> getBooksByLibrary(String libraryId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE library_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libraryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Book b = new Book(
                    rs.getString("title"), rs.getString("author"), rs.getString("category"), rs.getString("subject"),
                    rs.getString("location_row"), rs.getString("location_section"), rs.getString("location_block"),
                    rs.getString("image_path")
                );
                b.setAvailable(rs.getBoolean("is_available"));
                b.setCurrentBorrowingId(rs.getString("current_borrowing_id"));
                Date date = rs.getDate("borrowed_date");
                if (date != null) b.setBorrowedDate(date.toLocalDate());
                
                String borrower = rs.getString("borrowed_by_user");
                if (borrower != null) {
                    UserDAO uDao = new UserDAO();
                    b.setBorrowedBy(uDao.getUser(borrower));
                }
                books.add(b);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }
}