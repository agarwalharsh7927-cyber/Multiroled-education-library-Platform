import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String studentUsername;
    private String libraryId; // To locate the student
    private String itemName;
    private double amount;
    private String shippingAddress;
    private boolean isShipped;
    private LocalDate orderDate;

    public Order(String studentUsername, String libraryId, String itemName, double amount, String shippingAddress) {
        this.orderId = "ORD" + System.currentTimeMillis();
        this.studentUsername = studentUsername;
        this.libraryId = libraryId;
        this.itemName = itemName;
        this.amount = amount;
        this.shippingAddress = shippingAddress;
        this.isShipped = false;
        this.orderDate = LocalDate.now();
    }

    public String getOrderId() { return orderId; }
    public String getStudentUsername() { return studentUsername; }
    public String getLibraryId() { return libraryId; }
    public String getItemName() { return itemName; }
    public double getAmount() { return amount; }
    public String getShippingAddress() { return shippingAddress; }
    public boolean isShipped() { return isShipped; }
    public LocalDate getOrderDate() { return orderDate; }

    public void setShipped(boolean shipped) { isShipped = shipped; }

    @Override
    public String toString() {
        return "Order " + orderId + ": " + itemName + " for " + studentUsername + " (Shipped: " + isShipped + ")";
    }
}