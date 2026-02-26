import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Exercise 2: Airline service exposed through RMI.
 */
public interface AirlineService extends Remote {
    String getCompanyName() throws RemoteException;

    /** Returns all flights (including remaining seats). */
    List<Flight> listFlights() throws RemoteException;

    /**
     * Reserve one seat on a flight for a passenger.
     * Returns a Seat object if successful.
     */
    Seat reserveSeat(String flightId, String passengerName) throws RemoteException, BookingException;
}
