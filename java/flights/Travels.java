package flights;

/**
 * Marker interface for Flight and Itinerary.
 * This follows the Marker design pattern and
 * is designed to indicate that Flight and Itinerary are
 * Travel events that can be viewed by the user in an app.
 *
 * Interface guarantees that classes implementing Travels will have a
 * toString that can be used to list or display them.
 *
 * @author kael
 */
public interface Travels {
    public String toString();
}
