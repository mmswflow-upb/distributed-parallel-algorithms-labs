import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * In-memory airline implementation.
 * - holds flights in a map
 * - assigns seats sequentially
 */
public class AirlineServiceImpl extends UnicastRemoteObject implements AirlineService {

    private final String companyName;
    private final Map<String, Flight> flights = new LinkedHashMap<>();
    private final Map<String, Integer> nextSeatByFlight = new HashMap<>();

    public AirlineServiceImpl(String companyName, List<Flight> seedFlights) throws RemoteException {
        super();
        this.companyName = companyName;
        for (Flight f : seedFlights) {
            flights.put(f.getId(), f);
            nextSeatByFlight.put(f.getId(), 1);
        }
    }

    @Override
    public String getCompanyName() throws RemoteException {
        return companyName;
    }

    @Override
    public synchronized List<Flight> listFlights() throws RemoteException {
        // Return shallow copies so client sees current seat counts without mutating server state.
        List<Flight> out = new ArrayList<>();
        for (Flight f : flights.values()) {
            out.add(new Flight(f.getId(), f.getFrom(), f.getTo(), f.getFlightNumber(), f.getSeatsAvailable()));
        }
        return out;
    }

    @Override
    public synchronized Seat reserveSeat(String flightId, String passengerName)
            throws RemoteException, BookingException {

        if (passengerName == null || passengerName.trim().isEmpty()) {
            throw new BookingException("Passenger name is required.");
        }

        Flight f = flights.get(flightId);
        if (f == null) {
            throw new BookingException("Unknown flight id: " + flightId);
        }
        if (f.getSeatsAvailable() <= 0) {
            throw new BookingException("No seats left on " + f.getFlightNumber());
        }

        int seatIndex = nextSeatByFlight.getOrDefault(flightId, 1);
        nextSeatByFlight.put(flightId, seatIndex + 1);

        f.decrementSeat();

        // Simple seat numbering: 1A, 1B, ..., 1F then 2A...
        String seatNumber = toSeatNumber(seatIndex);
        return new Seat(companyName, f.getFlightNumber(), seatNumber, passengerName.trim());
    }

    private String toSeatNumber(int idx) {
        String[] letters = {"A","B","C","D","E","F"};
        int row = ((idx - 1) / letters.length) + 1;
        String letter = letters[(idx - 1) % letters.length];
        return row + letter;
    }
}
