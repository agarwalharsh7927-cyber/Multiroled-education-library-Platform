import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents a specific Library Branch/Instance.
 * Manages operations via JDBC DAOs and direct SQL queries for reporting.
 */
public class Library implements Serializable {
    private static final long serialVersionUID = 1L;

    private LibraryProfile profile;
    private String librarianUsername; 
    
    // DAOs for Database Operations
    private transient UserDAO userDAO = new UserDAO();
    private transient BookDAO bookDAO = new BookDAO();
    private transient TransactionDAO transactionDAO = new TransactionDAO();
    private transient LibraryDAO libDAO = new LibraryDAO();

    // Constants
    public static final int BORROWING_PERIOD_DAYS = 15;
    private static final int FINE_PER_DAY = 1;
    private static final int MAX_BORROWED_BOOKS = 3;
    private static final int REGISTRATION_FEE = 100;
    private static final String LIBRARIAN_ACCESS_KEY = "SECRET123"; 

    // Variables
    private int income = 0; 

    // Study Space Features
    private int seatCapacity = 20;
    private int slotPrice = 50;
    private List<String> slotTimings = new ArrayList<>(Arrays.asList("09:00 AM - 12:00 PM", "12:00 PM - 03:00 PM", "03:00 PM - 06:00 PM"));
    private transient Map<String, Map<String, List<String>>> seatBookings = new HashMap<>();

    public Library(LibraryProfile profile, String adminUser, String adminPass) {
        this.profile = profile;
        this.librarianUsername = adminUser;
    }
    
    public LibraryProfile getProfile() { return profile; }
    public String getLibrarianUsername() { return librarianUsername; }

    public <T> List<T> filterList(List<T> list, Predicate<T> condition) {
        return list.stream().filter(condition).collect(Collectors.toList());
    }

    // --- Operations ---

    public void addBook(String title, String author, String category, String subject,
                        String row, String section, String block, String imagePath) {
        Book book = new Book(title, author, category, subject, row, section, block, imagePath);
        bookDAO.addBook(book, profile.getLibraryId());
        transactionDAO.addTransaction(new TransactionRecord(null, book, ActivityType.BOOK_ADDED, "Added Book", 0), profile.getLibraryId());
    }
    
    public void removeBook(String title) throws LibraryActionException {
        bookDAO.deleteBook(title);
        transactionDAO.addTransaction(new TransactionRecord(null, null, ActivityType.BOOK_REMOVED, "Removed " + title, 0), profile.getLibraryId());
    }
    
    // FIX: Changed return type from void to String
    public String registerUser(String username, String password, String fullName, String gender,
                             String aadhaar, String phone, String address) throws LibraryActionException {
        if (userDAO.getUser(username) != null) {
            throw new LibraryActionException("Username '" + username + "' is already taken.");
        }
        
        List<String> errs = validatePassword(password);
        if (!errs.isEmpty()) throw new LibraryActionException("Password Invalid:\n" + String.join("\n", errs));

        User user = new User(username, password, fullName, gender, aadhaar, phone, address);
        userDAO.addUser(user, profile.getLibraryId());
        transactionDAO.addTransaction(new TransactionRecord(user, null, ActivityType.ACCOUNT_CREATED, "Registered", 0), profile.getLibraryId());
        
        return "(+) User registered successfully.";
    }
    
    public User authenticateUser(String username, String password) {
        User u = userDAO.getUser(username);
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }
    
    public boolean authenticateLibrarian(String username, String password) {
        return libDAO.authenticateLibrarian(username, password);
    }

    public String borrowBook(User user, String title) throws LibraryActionException {
        if(!user.hasPaidFees()) throw new LibraryActionException("Registration fee not paid.");
        
        if (user.getBorrowedBooks().size() >= MAX_BORROWED_BOOKS) {
            throw new LibraryActionException("Borrowing limit reached (" + MAX_BORROWED_BOOKS + " books).");
        }
        
        List<Book> books = bookDAO.getBooksByLibrary(profile.getLibraryId());
        Book book = null;
        for(Book b : books) if(b.getTitle().equalsIgnoreCase(title)) book = b;
        
        if (book == null) throw new LibraryActionException("Book not found.");
        if (!book.isAvailable()) throw new LibraryActionException("Book is currently borrowed.");
        
        String nextReserver = book.getNextReserver();
        if (nextReserver != null && !nextReserver.equals(user.getUsername())) {
            throw new LibraryActionException("This book is reserved for user: " + nextReserver);
        }
        if (nextReserver != null && nextReserver.equals(user.getUsername())) {
            book.removeNextReserver();
        }
        
        book.setAvailable(false);
        book.setBorrowedBy(user);
        book.setBorrowedDate(LocalDate.now());
        book.setCurrentBorrowingId("TXN" + System.currentTimeMillis());
        
        bookDAO.updateBookStatus(book);
        transactionDAO.addTransaction(new TransactionRecord(user, book, ActivityType.BOOK_BORROWED, "Borrowed", 0), profile.getLibraryId());
        
        return "(+) Borrowed Successfully. ID: " + book.getCurrentBorrowingId();
    }

    public String returnBook(User user, String title) throws LibraryActionException {
        List<Book> books = bookDAO.getBooksByLibrary(profile.getLibraryId());
        Book book = null;
        for(Book b : books) if(b.getTitle().equalsIgnoreCase(title)) book = b;

        if(book == null) throw new LibraryActionException("Book not found.");

        long daysBetween = ChronoUnit.DAYS.between(book.getBorrowedDate(), LocalDate.now());
        String message = "(+) Book returned.";
        int fine = 0;

        if (daysBetween > BORROWING_PERIOD_DAYS) {
            long overdueDays = daysBetween - BORROWING_PERIOD_DAYS;
            fine = (int) (overdueDays * FINE_PER_DAY);
            income += fine; 
            message = "(!) Returned " + overdueDays + " days late. Fine: ₹" + fine;
            transactionDAO.addTransaction(new TransactionRecord(user, book, ActivityType.LATE_FINE_PAID, "Fine", fine), profile.getLibraryId());
        }

        book.setAvailable(true);
        book.setBorrowedBy(null);
        book.setBorrowedDate(null);
        book.setCurrentBorrowingId(null);

        bookDAO.updateBookStatus(book);
        transactionDAO.addTransaction(new TransactionRecord(user, book, ActivityType.BOOK_RETURNED, "Returned", 0), profile.getLibraryId());
        
        if (book.getNextReserver() != null) {
            message += "\n(i) ATTENTION: Reserved for user: " + book.getNextReserver();
        }
        
        return message;
    }
    
    public String reserveBook(User user, String title) throws LibraryActionException {
        transactionDAO.addTransaction(new TransactionRecord(user, null, ActivityType.BOOK_RESERVED, "Reserved " + title, 0), profile.getLibraryId());
        return "(+) Reservation recorded.";
    }
    
    // --- Seat Booking ---
    public synchronized String bookSeat(User user, LocalDate date, String slot) throws LibraryActionException {
        if (seatBookings == null) seatBookings = new HashMap<>();
        
        String dateKey = date.toString();
        seatBookings.putIfAbsent(dateKey, new HashMap<>());
        Map<String, List<String>> dailySlots = seatBookings.get(dateKey);
        dailySlots.putIfAbsent(slot, new ArrayList<>());
        
        List<String> bookedStudents = dailySlots.get(slot);
        
        if (bookedStudents.contains(user.getUsername())) throw new LibraryActionException("Seat already booked.");
        if (bookedStudents.size() >= seatCapacity) throw new LibraryActionException("Slot full.");
        
        bookedStudents.add(user.getUsername());
        income += slotPrice; 
        
        transactionDAO.addTransaction(new TransactionRecord(user, null, ActivityType.SEAT_BOOKED, "Seat: " + dateKey + " " + slot, slotPrice), profile.getLibraryId());
        
        return "(+) Seat booked! Cost: ₹" + slotPrice;
    }
    
    public int getAvailableSeats(LocalDate date, String slot) {
        if (seatBookings == null) seatBookings = new HashMap<>();
        String dateKey = date.toString();
        if (!seatBookings.containsKey(dateKey)) return seatCapacity;
        if (!seatBookings.get(dateKey).containsKey(slot)) return seatCapacity;
        return seatCapacity - seatBookings.get(dateKey).get(slot).size();
    }

    // --- Read Operations ---

    public List<Book> getAllBooks() {
        return bookDAO.getBooksByLibrary(profile.getLibraryId());
    }
    
    public List<User> getAllUsers() {
        return userDAO.getUsersByLibrary(profile.getLibraryId());
    }
    
    public Book searchBook(String t) {
        for(Book b : getAllBooks()) if(b.getTitle().equalsIgnoreCase(t)) return b;
        return null;
    }
    
    public List<TransactionRecord> getAllTransactionRecords() {
        List<TransactionRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE library_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profile.getLibraryId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String typeStr = rs.getString("activity_type");
                ActivityType type = ActivityType.valueOf(typeStr);
                TransactionRecord tr = new TransactionRecord(
                    null, null, type, rs.getString("details"), rs.getDouble("amount")
                );
                list.add(tr);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    public Map<String, String> getStats() {
        Map<String, String> s = new HashMap<>();
        s.put("Books", String.valueOf(getAllBooks().size()));
        s.put("Users", String.valueOf(getAllUsers().size()));
        s.put("Capacity", String.valueOf(seatCapacity));
        
        double totalIncome = income; 
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE library_id=?")) {
            ps.setString(1, profile.getLibraryId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) totalIncome = rs.getDouble(1);
        } catch(Exception e) {}
        
        s.put("Income", "₹" + totalIncome);
        return s;
    }
    
    public String collectFees(User user) {
        userDAO.updateUserFees(user.getUsername(), true, user.getELibraryExpenditure());
        income += REGISTRATION_FEE; 
        transactionDAO.addTransaction(new TransactionRecord(user, null, ActivityType.REGISTRATION_FEE_PAID, "Fee Paid", REGISTRATION_FEE), profile.getLibraryId());
        return "(+) Fee collected.";
    }

    public int getRegistrationFeeAmount() { return REGISTRATION_FEE; }
    public List<String> getSlotTimings() { return slotTimings; }
    
    public String exportTransactionsToCSV(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath));
             Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactions WHERE library_id=?")) {
             
            ps.setString(1, profile.getLibraryId());
            ResultSet rs = ps.executeQuery();
            
            writer.println("ID,Username,Book,Type,Date,Details,Amount");
            while(rs.next()) {
                writer.printf("%s,%s,%s,%s,%s,%s,%.2f%n",
                    rs.getString("transaction_id"), rs.getString("username"), rs.getString("book_title"),
                    rs.getString("activity_type"), rs.getDate("date"), rs.getString("details"), rs.getDouble("amount")
                );
            }
            return "(+) Exported successfully to " + filePath;
        } catch (Exception e) {
            return "(!) Export failed: " + e.getMessage();
        }
    }
    
    // --- Admin Updates ---
    
    public String resetLibrarianCredentialsWithAccessKey(String enteredAccessKey, String newUsername, String newPassword) {
        if (!LIBRARIAN_ACCESS_KEY.equals(enteredAccessKey)) return "(!) Invalid Access Key.";
        List<String> errs = validatePassword(newPassword);
        if(!errs.isEmpty()) return "(!) Password Invalid:\n" + String.join("\n", errs);
        
        this.librarianUsername = newUsername;
        return "(+) Librarian credentials reset.";
    }
    
    public String updateStudentCredentials(String u, String nu, String np) {
         if(np != null && !np.isEmpty()) {
            List<String> errs = validatePassword(np);
            if(!errs.isEmpty()) return "(!) Password Invalid:\n" + String.join("\n", errs);
         }
        return "(+) Updated (DB Logic required in UserDAO)";
    }
    
    public String deleteStudent(String u) {
        userDAO.deleteUser(u);
        return "(-) Student deleted.";
    }

    private List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        if (password == null || password.length() < 8 || password.length() > 16) errors.add("Password must be 8-16 chars.");
        if (!Pattern.compile(".*[A-Z].*").matcher(password).matches()) errors.add("Must contain uppercase.");
        if (!Pattern.compile(".*[a-z].*").matcher(password).matches()) errors.add("Must contain lowercase.");
        if (!Pattern.compile(".*[0-9].*").matcher(password).matches()) errors.add("Must contain digit.");
        if (!Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*").matcher(password).matches()) errors.add("Must contain special char.");
        return errors;
    }
}