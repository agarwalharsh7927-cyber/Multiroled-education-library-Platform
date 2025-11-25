/**
 * Custom Exception class to handle specific library operation failures.
 * OOP Concept: Exception Handling & Inheritance (extends Exception)
 */
public class LibraryActionException extends Exception {
    // Added serialVersionUID to remove the warning "The serializable class LibraryActionException does not declare a static final serialVersionUID field of type long"
    private static final long serialVersionUID = 1L;

    public LibraryActionException(String message) {
        super(message);
    }
}