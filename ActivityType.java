/**
 * Enum for different types of activities that can be logged.
 */
public enum ActivityType {
    // Basic Book Operations
    BOOK_BORROWED("Book Borrowed"),
    BOOK_RETURNED("Book Returned"),
    
    // Financials
    REGISTRATION_FEE_PAID("Reg. Fee Paid"),
    LATE_FINE_PAID("Late Fine Paid"),
    
    // Account Management
    ACCOUNT_CREATED("Account Created"),
    ACCOUNT_DELETED("Account Deleted"),
    STUDENT_CREDENTIALS_UPDATED("Student Credentials Updated"),
    STUDENT_PASSWORD_RESET("Student Password Reset"),
    LIBRARIAN_CREDENTIALS_UPDATED("Librarian Credentials Updated"),
    
    // Inventory Management
    BOOK_ADDED("Book Added"),
    BOOK_EDITED("Book Edited"),
    BOOK_REMOVED("Book Removed"),
    
    // --- NEW FEATURES ---
    
    // Multi-Library Registration
    LIBRARY_REGISTERED("Library Registered"),
    LIBRARY_APPROVED("Library Approved"),
    
    // Study Space
    SEAT_BOOKED("Seat Booked"),
    
    // Advanced Book Features
    BOOK_RESERVED("Book Reserved"),
    
    // eLibrary & eCommerce
    EBOOK_PURCHASED("eBook Purchased/Downloaded"),
    PHYSICAL_BOOK_ORDERED("Physical Book Ordered"),
    ORDER_SHIPPED("Order Shipped");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}