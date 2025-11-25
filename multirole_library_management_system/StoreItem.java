import java.io.Serializable;

public class StoreItem implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ItemType { PDF, PHYSICAL }

    private String id;
    private String title;
    private String author;
    private double price;
    private ItemType type;
    private String contentPath; // File path for PDF or Image
    private String description;

    public StoreItem(String title, String author, double price, ItemType type, String contentPath, String description) {
        this.id = "ITM" + System.currentTimeMillis();
        this.title = title;
        this.author = author;
        this.price = price;
        this.type = type;
        this.contentPath = contentPath;
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public ItemType getType() { return type; }
    public String getContentPath() { return contentPath; }
    public String getDescription() { return description; }

    // Setters
    public void setPrice(double price) { this.price = price; }
    public void setContentPath(String path) { this.contentPath = path; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }

    @Override
    public String toString() {
        return title + " (" + type + ") - ₹" + price;
    }
}