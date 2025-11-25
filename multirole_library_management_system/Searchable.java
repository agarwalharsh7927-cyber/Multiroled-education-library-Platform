/**
 * Interface to define search behavior for items.
 * OOP Concept: Interfaces
 */
public interface Searchable {
    // Returns true if the object matches the query string
    boolean matches(String query);
}
