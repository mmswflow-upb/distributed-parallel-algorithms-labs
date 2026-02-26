import java.io.Serializable;

/** Checked exception for booking errors (no seats, unknown flight, etc.). */
public class BookingException extends Exception implements Serializable {
    public BookingException(String message) {
        super(message);
    }
}
