import java.rmi.Naming;
import java.util.*;

/**
 * Start an airline server and bind it into the RMI registry.
 *
 * Usage examples:
 *   java AirlineServer AirlineA AirlineA
 *   java AirlineServer AirlineB AirlineB
 *
 * It binds to: rmi://localhost:1099/<bindName>
 */
public class AirlineServer {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java AirlineServer <companyName> <bindName>");
            System.exit(1);
        }

        String company = args[0];
        String bindName = args[1];

        try {
            List<Flight> flights = seedFlights(company);
            AirlineService svc = new AirlineServiceImpl(company, flights);

            String url = "rmi://localhost:1099/" + bindName;
            Naming.rebind(url, svc);

            System.out.println("Bound airline '" + company + "' at " + url);
            System.out.println("Flights:");
            for (Flight f : svc.listFlights()) {
                System.out.println(" - " + f.getId() + " : " + f);
            }
            System.out.println("\nServer running. Ctrl+C to stop.");
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
            e.printStackTrace();
        }
    }

    private static List<Flight> seedFlights(String company) {
        // A couple of sample flights per company. IDs must be unique *within* the company.
        List<Flight> flights = new ArrayList<>();
        if ("AirlineA".equalsIgnoreCase(company)) {
            flights.add(new Flight("A1", "BEY", "PAR", "A100", 5));
            flights.add(new Flight("A2", "PAR", "NYC", "A200", 4));
            flights.add(new Flight("A3", "BEY", "DXB", "A300", 6));
        } else if ("AirlineB".equalsIgnoreCase(company)) {
            flights.add(new Flight("B1", "BEY", "ROM", "B110", 5));
            flights.add(new Flight("B2", "ROM", "NYC", "B210", 4));
            flights.add(new Flight("B3", "DXB", "SIN", "B310", 6));
        } else {
            // Generic defaults
            flights.add(new Flight(company + "-1", "AAA", "BBB", company.substring(0,1).toUpperCase() + "101", 5));
            flights.add(new Flight(company + "-2", "BBB", "CCC", company.substring(0,1).toUpperCase() + "201", 4));
        }
        return flights;
    }
}
