import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

/**
 * Simple GUI client that can reserve seats from multiple airline companies via RMI.
 *
 * By reserving seats from different airlines, you effectively build a multi-airline itinerary.
 *
 * Default bindings (editable in code): AirlineA and AirlineB.
 */
public class FlightsClientGUI {

    private static final String[] DEFAULT_BINDINGS = {"AirlineA", "AirlineB"};

    private final DefaultListModel<String> airlineModel = new DefaultListModel<>();
    private final DefaultListModel<Flight> flightModel = new DefaultListModel<>();
    private final DefaultListModel<Seat> itineraryModel = new DefaultListModel<>();

    private AirlineService currentService;

    private final JTextField passengerField = new JTextField(20);
    private final JLabel status = new JLabel(" ");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightsClientGUI().start());
    }

    private void start() {
        JFrame frame = new JFrame("Aerial Flights Booking (RMI)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 550);
        frame.setLocationRelativeTo(null);

        // Left: airlines
        JList<String> airlines = new JList<>(airlineModel);
        airlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane airlinePane = new JScrollPane(airlines);
        airlinePane.setBorder(BorderFactory.createTitledBorder("Airlines (RMI bindings)"));

        // Center: flights
        JList<Flight> flights = new JList<>(flightModel);
        flights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane flightsPane = new JScrollPane(flights);
        flightsPane.setBorder(BorderFactory.createTitledBorder("Flights"));

        // Right: itinerary
        JList<Seat> itinerary = new JList<>(itineraryModel);
        JScrollPane itinPane = new JScrollPane(itinerary);
        itinPane.setBorder(BorderFactory.createTitledBorder("Your itinerary (can include multiple airlines)"));

        // Controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Passenger:"));
        controls.add(passengerField);

        JButton refreshBtn = new JButton("Refresh flights");
        JButton reserveBtn = new JButton("Reserve selected flight");
        JButton clearBtn = new JButton("Clear itinerary");

        controls.add(refreshBtn);
        controls.add(reserveBtn);
        controls.add(clearBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(controls, BorderLayout.CENTER);
        bottom.add(status, BorderLayout.SOUTH);

        JSplitPane leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, airlinePane, flightsPane);
        leftSplit.setResizeWeight(0.25);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, itinPane);
        mainSplit.setResizeWeight(0.65);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(mainSplit, BorderLayout.CENTER);
        frame.getContentPane().add(bottom, BorderLayout.SOUTH);

        // load default airline bindings
        for (String b : DEFAULT_BINDINGS) airlineModel.addElement(b);

        airlines.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String bind = airlines.getSelectedValue();
                if (bind != null) connectAndLoadFlights(bind);
            }
        });

        refreshBtn.addActionListener(e -> {
            String bind = airlines.getSelectedValue();
            if (bind != null) connectAndLoadFlights(bind);
        });

        reserveBtn.addActionListener(e -> {
            Flight selected = flights.getSelectedValue();
            if (selected == null || currentService == null) {
                setStatus("Select an airline and a flight.");
                return;
            }
            String passenger = passengerField.getText();
            try {
                Seat seat = currentService.reserveSeat(selected.getId(), passenger);
                itineraryModel.addElement(seat);
                setStatus("Reserved: " + seat);
                // refresh so seat counts update
                connectAndLoadFlights(airlines.getSelectedValue());
            } catch (BookingException be) {
                setStatus("Booking failed: " + be.getMessage());
            } catch (Exception ex) {
                setStatus("Error: " + ex);
            }
        });

        clearBtn.addActionListener(e -> itineraryModel.clear());

        frame.setVisible(true);

        // auto-select first airline
        if (!airlineModel.isEmpty()) airlines.setSelectedIndex(0);
    }

    private void connectAndLoadFlights(String bindName) {
        try {
            currentService = (AirlineService) Naming.lookup("rmi://localhost:1099/" + bindName);
            List<Flight> flights = currentService.listFlights();

            flightModel.clear();
            for (Flight f : flights) flightModel.addElement(f);

            setStatus("Connected to " + currentService.getCompanyName() + " (" + bindName + ")");
        } catch (Exception e) {
            currentService = null;
            flightModel.clear();
            setStatus("Failed to connect to '" + bindName + "': " + e);
        }
    }

    private void setStatus(String msg) {
        status.setText(" " + msg);
    }
}
