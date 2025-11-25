import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single transaction or activity log in the library.
 */
public class TransactionRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String transactionId;
    private final User student; // Can be null for librarian-only actions
    private final Book book;    // Can be null for non-book related activities (e.g., Seat booking, eBook purchase)
    private final ActivityType type;
    private final LocalDate date;
    private final String details; // e.g., "Borrowed 'The Great Gatsby'", "Paid fine of Rs. 10", "Purchased Java Guide PDF"
    private final double amount;  // e.g., fine amount, fee amount, store item price (0 if not applicable)
    private static int nextId = 1;

    public TransactionRecord(User student, Book book, ActivityType type, String details, double amount) {
        this.transactionId = String.format("ACT%07d", nextId++);
        this.student = student;
        this.book = book;
        this.type = type;
        this.date = LocalDate.now(); // Automatically set to current date
        this.details = details;
        this.amount = amount;
    }

    // --- Getters ---
    public String getTransactionId() { return transactionId; }
    public User getStudent() { return student; }
    public Book getBook() { return book; }
    public ActivityType getType() { return type; }
    public LocalDate getDate() { return date; }
    public String getDetails() { return details; }
    public double getAmount() { return amount; }

    public String getBookTitleSafe() {
        return book != null ? book.getTitle() : "N/A";
    }
     public String getStudentUsernameSafe() {
        return student != null ? student.getUsername() : "N/A (Admin Action)";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - User: %s, Type: %s, Details: %s, Amount: %.2f, Book: %s",
                transactionId,
                date.format(DateTimeFormatter.ISO_DATE),
                getStudentUsernameSafe(),
                type.getDisplayName(),
                details,
                amount,
                getBookTitleSafe());
    }
}