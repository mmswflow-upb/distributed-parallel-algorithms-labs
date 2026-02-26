import java.rmi.Naming;

/**
 * Server that binds the CalculatorService into the RMI registry.
 */
public class CalculatorServer {
    public static final String BIND_URL = "rmi://localhost:1099/CalculatorService";

    public CalculatorServer() {
        try {
            Calculator c = new CalculatorImpl();
            Naming.rebind(BIND_URL, c);
            System.out.println("CalculatorService bound at " + BIND_URL);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CalculatorServer();
    }
}
