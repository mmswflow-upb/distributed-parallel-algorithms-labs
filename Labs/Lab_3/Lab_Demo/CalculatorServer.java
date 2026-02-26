package Lab_Demo;
import java.rmi.Naming;

/**
 * Server that binds the CalculatorService into the RMI registry.
 */
public class CalculatorServer {
    public CalculatorServer() {
        try {
            Calculator c = new CalculatorImpl();
            Naming.rebind("rmi://localhost:1099/CalculatorService", c);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    public static void main(String[] args) {
        new CalculatorServer();
    }
}
