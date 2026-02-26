import java.io.Serializable;

/**
 * A simple serializable Flight DTO.
 */
public class Flight implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;            // unique ID used for booking
    private final String from;
    private final String to;
    private final String flightNumber;
    private int seatsAvailable;

    public Flight(String id, String from, String to, String flightNumber, int seatsAvailable) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.flightNumber = flightNumber;
        this.seatsAvailable = seatsAvailable;
    }

    public String getId() { return id; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getFlightNumber() { return flightNumber; }
    public int getSeatsAvailable() { return seatsAvailable; }

    public void decrementSeat() { seatsAvailable--; }

    @Override
    public String toString() {
        return flightNumber + " " + from + " -> " + to + " (seats left: " + seatsAvailable + ")";
    }
}
