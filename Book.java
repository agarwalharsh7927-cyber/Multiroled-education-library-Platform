import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a book in the library.
 * OOP Concept: Implementing Interfaces (Searchable)
 */
public class Book implements Serializable, Searchable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private String category;
    private String subject;
    private boolean isAvailable;
    private User borrowedBy;
    private LocalDate borrowedDate;
    private String currentBorrowingId;
    private String locationRow;
    private String locationSection;
    private String locationBlock;
    private String imagePath; 
    
    private List<String> reservationQueue;

    public Book(String title, String author, String category, String subject,
                String locationRow, String locationSection, String locationBlock, String imagePath) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.subject = subject;
        this.locationRow = locationRow;
        this.locationSection = locationSection;
        this.locationBlock = locationBlock;
        this.imagePath = imagePath;
        this.isAvailable = true;
        this.borrowedBy = null;
        this.borrowedDate = null;
        this.currentBorrowingId = null;
        this.reservationQueue = new ArrayList<>();
    }

    // --- Interface Implementation (Polymorphism) ---
    @Override
    public boolean matches(String query) {
        if (query == null) return false;
        String q = query.toLowerCase();
        return title.toLowerCase().contains(q) || 
               author.toLowerCase().contains(q) || 
               category.toLowerCase().contains(q) || 
               subject.toLowerCase().contains(q);
    }

    // --- Getters & Setters ---
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getSubject() { return subject; }
    public boolean isAvailable() { return isAvailable; }
    public User getBorrowedBy() { return borrowedBy; }
    public LocalDate getBorrowedDate() { return borrowedDate; }
    public String getCurrentBorrowingId() { return currentBorrowingId; }
    public String getLocationRow() { return locationRow; }
    public String getLocationSection() { return locationSection; }
    public String getLocationBlock() { return locationBlock; }
    public String getImagePath() { return imagePath; }
    public List<String> getReservationQueue() { return reservationQueue; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setBorrowedBy(User user) { this.borrowedBy = user; }
    public void setBorrowedDate(LocalDate date) { this.borrowedDate = date; }
    public void setCurrentBorrowingId(String id) { this.currentBorrowingId = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setLocationRow(String locationRow) { this.locationRow = locationRow; }
    public void setLocationSection(String locationSection) { this.locationSection = locationSection; }
    public void setLocationBlock(String locationBlock) { this.locationBlock = locationBlock; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public void addReservation(String username) {
        if (!reservationQueue.contains(username)) {
            reservationQueue.add(username);
        }
    }
    
    public String getNextReserver() {
        return reservationQueue.isEmpty() ? null : reservationQueue.get(0);
    }
    
    public void removeNextReserver() {
        if (!reservationQueue.isEmpty()) {
            reservationQueue.remove(0);
        }
    }

    @Override
    public String toString() {
        return String.format("%s by %s (%s)", title, author, isAvailable ? "Available" : "Borrowed");
    }
}