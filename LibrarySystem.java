import java.io.Serializable;
import java.util.*;

/**
 * The central system controller that manages multiple libraries, the super admin, 
 * and the global eLibrary inventory via JDBC DAOs.
 */
public class LibrarySystem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // DAOs for Database Operations
    private transient LibraryDAO libraryDAO = new LibraryDAO();
    private transient StoreDAO storeDAO = new StoreDAO();
    
    // In-memory cache for store items (populated from DB)
    private List<StoreItem> storeItems;

    private String superAdminUsername = "superadmin";
    private String superAdminPassword = "super123";

    public LibrarySystem() {
        // Load store items from DB on initialization
        this.storeItems = storeDAO.getAllItems();
        
        // Optional: Insert default items if DB is empty (for demo purposes)
        if (this.storeItems.isEmpty()) {
            StoreItem javaGuide = new StoreItem("Java Programming Guide", "James Gosling", 50.0, StoreItem.ItemType.PDF, "java_guide.pdf", "A comprehensive guide to Java.");
            StoreItem dsGuide = new StoreItem("Data Structures 101", "Alice Smith", 75.0, StoreItem.ItemType.PDF, "ds_101.pdf", "Learn basics of Data Structures.");
            StoreItem atlas = new StoreItem("Physical Atlas", "Map Makers Inc.", 500.0, StoreItem.ItemType.PHYSICAL, "atlas_cover.jpg", "World Atlas Hardcover.");
            
            // Note: In a real app, you'd have an addStoreItem method in StoreDAO.
            // For this demo, we just cache them in memory if DB insert isn't explicit here.
            this.storeItems.add(javaGuide);
            this.storeItems.add(dsGuide);
            this.storeItems.add(atlas);
        }
    }
    
    // Persistence methods (File-based save is deprecated by DB, kept for interface compatibility)
    public static void saveData(LibrarySystem sys, String f) {} 
    public static LibrarySystem loadData(String f) { return new LibrarySystem(); }

    // --- Super Admin Logic ---
    public boolean authenticateSuperAdmin(String u, String p) {
        return superAdminUsername.equals(u) && superAdminPassword.equals(p);
    }

    public List<LibraryProfile> getPendingRequests() {
        List<LibraryProfile> all = libraryDAO.getAllLibraries();
        List<LibraryProfile> pending = new ArrayList<>();
        for(LibraryProfile p : all) {
            if(!p.isApproved()) pending.add(p);
        }
        return pending;
    }

    public void approveLibrary(String id, String u, String p) {
        libraryDAO.approveLibrary(id, u, p);
    }
    
    public LibraryProfile createRegistrationRequest(String name, String addr, String link, String photo, String owner, String phone, String email, String aadhaar) {
        LibraryProfile p = new LibraryProfile(name, addr, link, photo, owner, phone, email, aadhaar);
        libraryDAO.registerLibrary(p);
        return p;
    }
    
    public boolean processRegistrationPayment(String id, double amt) { 
        // In a real app, this would update a transaction table
        return true; 
    }

    // --- General Access ---
    public List<Library> getAllLibraries() {
        List<Library> libs = new ArrayList<>();
        List<LibraryProfile> profiles = libraryDAO.getAllLibraries();
        for(LibraryProfile p : profiles) {
            if(p.isApproved()) {
                // Reconstruct Library object with profile (DAOs inside Library handle the rest)
                libs.add(new Library(p, null, null));
            }
        }
        return libs;
    }
    
    public Library getLibrary(String id) {
        for(Library lib : getAllLibraries()) {
            if(lib.getProfile().getLibraryId().equals(id)) return lib;
        }
        return null;
    }

    // --- eLibrary / eCommerce Logic ---
    public List<StoreItem> getStoreItems() { 
        return storeItems; 
    }
    
    public void addStoreItem(StoreItem item) {
        // In real implementation: storeDAO.addItem(item);
        storeItems.add(item);
    }
    
    public void removeStoreItem(StoreItem item) {
        // In real implementation: storeDAO.removeItem(item.getId());
        storeItems.remove(item);
    }

    public void placeOrder(Order o) {
        storeDAO.addOrder(o);
    }
    
    public List<Order> getGlobalOrders() {
        // In a real app: return storeDAO.getAllOrders();
        return new ArrayList<>(); // Placeholder return
    }
    
    public void updateOrderStatus(String orderId, boolean isShipped) {
        // In a real app: storeDAO.updateOrderStatus(orderId, isShipped);
    }
}