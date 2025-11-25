import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user (student) of the library.
 * OOP Concept: Inheritance (Extends Person)
 */
public class User extends Person { 
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private final String gender;
    private final String aadhaarNumber;
    private final List<Book> borrowedBooks;
    private boolean hasPaidFees;
    
    // --- Fields for eLibrary ---
    private double eLibraryExpenditure;
    private List<Order> myOrders;

    public User(String username, String password, String fullName, String gender, String aadhaarNumber,
                String phoneNumber, String address) {
        super(fullName, phoneNumber, address); // Call parent constructor
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.aadhaarNumber = aadhaarNumber;
        this.borrowedBooks = new ArrayList<>();
        this.hasPaidFees = false;
        this.eLibraryExpenditure = 0.0;
        this.myOrders = new ArrayList<>();
    }

    // OOP Concept: Polymorphism (Implementation of abstract method)
    @Override
    public String getRole() {
        return "Student";
    }

    // --- Getters ---
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }
    public String getAadhaarNumber() { return aadhaarNumber; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
    public boolean hasPaidFees() { return hasPaidFees; }
    public double getELibraryExpenditure() { return eLibraryExpenditure; }
    public List<Order> getMyOrders() { return myOrders; }

    // --- Setters ---
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }


    // --- Actions ---
    public void payFees() { hasPaidFees = true; }
    public void borrowBook(Book book) { borrowedBooks.add(book); }
    public void returnBook(Book book) { borrowedBooks.remove(book); }
    
    public void addExpenditure(double amount) {
        this.eLibraryExpenditure += amount;
    }
    
    public void addOrder(Order order) {
        this.myOrders.add(order);
    }

     @Override
    public String toString() { 
        return "User{" +
               "username='" + username + '\'' +
               ", fullName='" + fullName + '\'' +
               '}';
    }
}