import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {
    private Connection conn;
    
    public StoreDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    public List<StoreItem> getAllItems() {
        List<StoreItem> items = new ArrayList<>();
        String sql = "SELECT * FROM store_items";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StoreItem.ItemType type = rs.getString("item_type").equals("PDF") ? StoreItem.ItemType.PDF : StoreItem.ItemType.PHYSICAL;
                items.add(new StoreItem(
                    rs.getString("title"), 
                    rs.getString("author"), 
                    rs.getDouble("price"), 
                    type, 
                    rs.getString("content_path"), 
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return items;
    }
    
    public void addOrder(Order order) {
        String sql = "INSERT INTO orders (order_id, student_username, library_id, item_name, amount, shipping_address, order_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getOrderId());
            ps.setString(2, order.getStudentUsername());
            ps.setString(3, order.getLibraryId());
            ps.setString(4, order.getItemName());
            ps.setDouble(5, order.getAmount());
            ps.setString(6, order.getShippingAddress());
            ps.setDate(7, Date.valueOf(order.getOrderDate()));
            ps.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
}