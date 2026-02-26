import java.io.Serializable;

/**
 * Seat is what the client receives after a successful reservation.
 */
public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String company;
    private final String flightNumber;
    private final String seatNumber;
    private final String passengerName;

    public Seat(String company, String flightNumber, String seatNumber, String passengerName) {
        this.company = company;
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.passengerName = passengerName;
    }

    public String getCompany() { return company; }
    public String getFlightNumber() { return flightNumber; }
    public String getSeatNumber() { return seatNumber; }
    public String getPassengerName() { return passengerName; }

    @Override
    public String toString() {
        return company + " " + flightNumber + " — Seat " + seatNumber + " — " + passengerName;
    }
}
