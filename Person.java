import java.io.Serializable;

/**
 * Abstract base class representing a generic person.
 * OOP Concept: Abstraction & Inheritance
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String fullName;
    protected String phoneNumber;
    protected String address;

    public Person(String fullName, String phoneNumber, String address) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Abstract method forcing subclasses to implement string representation
    public abstract String getRole();

    // Getters
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
}