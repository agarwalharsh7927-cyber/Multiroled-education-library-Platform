import java.io.Serializable;

public class LibraryProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String libraryId;
    private String libraryName;
    private String address;
    private String locationLink;
    private String photoPath;
    
    private String ownerName;
    private String ownerPhone;
    private String ownerEmail;
    private String ownerAadhaar;
    
    private boolean isApproved;
    private boolean paymentDone; // Registration payment (20 Rs)

    public LibraryProfile(String libraryName, String address, String locationLink, String photoPath,
                          String ownerName, String ownerPhone, String ownerEmail, String ownerAadhaar) {
        this.libraryName = libraryName;
        this.address = address;
        this.locationLink = locationLink;
        this.photoPath = photoPath;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.ownerEmail = ownerEmail;
        this.ownerAadhaar = ownerAadhaar;
        this.isApproved = false;
        this.paymentDone = false;
        this.libraryId = "LIB" + System.currentTimeMillis();
    }

    // --- Getters (These fix the "field not used" warnings) ---
    public String getLibraryId() { return libraryId; }
    public String getLibraryName() { return libraryName; }
    public String getAddress() { return address; }
    public String getLocationLink() { return locationLink; }
    public String getPhotoPath() { return photoPath; }
    
    public String getOwnerName() { return ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public String getOwnerEmail() { return ownerEmail; }
    public String getOwnerAadhaar() { return ownerAadhaar; }
    
    public boolean isApproved() { return isApproved; }
    public boolean isPaymentDone() { return paymentDone; }
    
    // --- Setters ---
    public void setApproved(boolean approved) { isApproved = approved; }
    public void setPaymentDone(boolean paymentDone) { this.paymentDone = paymentDone; }

    @Override
    public String toString() {
        return libraryName + " (" + address + ")";
    }
}